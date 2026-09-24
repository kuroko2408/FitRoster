package com.fitroster.workoutbuilder.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitroster.workoutbuilder.data.LogMealRequest
import com.fitroster.workoutbuilder.data.MacroTotalsDto
import com.fitroster.workoutbuilder.data.NutritionApi
import com.fitroster.workoutbuilder.data.NutritionSummaryDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AthleteNutritionUiState(
    val summary: NutritionSummaryDto? = null,
    val foodName: String = "",
    val protein: String = "",
    val carbohydrates: String = "",
    val fats: String = "",
    val calories: String = "",
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val message: String? = null,
    val isError: Boolean = false,
)

class AthleteNutritionViewModel(
    private val api: NutritionApi,
    private val athleteId: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AthleteNutritionUiState())
    val uiState = _uiState.asStateFlow()

    init { refreshSummary() }

    fun updateFoodName(value: String) = update { copy(foodName = value, message = null) }
    fun updateProtein(value: String) = update { copy(protein = value, message = null) }
    fun updateCarbohydrates(value: String) = update { copy(carbohydrates = value, message = null) }
    fun updateFats(value: String) = update { copy(fats = value, message = null) }
    fun updateCalories(value: String) = update { copy(calories = value, message = null) }

    fun refreshSummary() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null) }
            try {
                val result = api.getDailySummary(athleteId)
                _uiState.update { it.copy(summary = result, isLoading = false, isError = false) }
            } catch (error: Exception) {
                _uiState.update { it.copy(isLoading = false, message = error.message ?: "Could not load today's summary.", isError = true) }
            }
        }
    }

    fun logMeal() {
        val state = _uiState.value
        val protein = state.protein.toDoubleOrNull()
        val carbs = state.carbohydrates.toDoubleOrNull()
        val fats = state.fats.toDoubleOrNull()
        val calories = state.calories.toDoubleOrNull()
        if (state.foodName.isBlank() || listOf(protein, carbs, fats, calories).any { it == null || it < 0.0 }) {
            _uiState.update { it.copy(message = "Add a meal name and valid non-negative macro amounts.", isError = true) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, message = null, isError = false) }
            try {
                api.logMeal(
                    LogMealRequest(
                        athleteId = athleteId,
                        foodName = state.foodName.trim(),
                        protein = protein!!,
                        carbs = carbs!!,
                        fats = fats!!,
                        calories = calories!!,
                    ),
                )
                val summary = api.getDailySummary(athleteId)
                _uiState.update {
                    it.copy(
                        summary = summary,
                        foodName = "",
                        protein = "",
                        carbohydrates = "",
                        fats = "",
                        calories = "",
                        isSaving = false,
                        message = "Meal logged.",
                        isError = false,
                    )
                }
            } catch (error: Exception) {
                _uiState.update { it.copy(isSaving = false, message = error.message ?: "Could not log meal.", isError = true) }
            }
        }
    }

    private fun update(transform: AthleteNutritionUiState.() -> AthleteNutritionUiState) = _uiState.update(transform)
}
