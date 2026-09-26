package com.fitroster.workoutbuilder.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitroster.workoutbuilder.data.AssignTargetsRequest
import com.fitroster.workoutbuilder.data.NutritionApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CoachNutritionUiState(
    val protein: String = "",
    val carbohydrates: String = "",
    val fats: String = "",
    val calories: String = "",
    val isSaving: Boolean = false,
    val message: String? = null,
    val isError: Boolean = false,
)

class CoachNutritionViewModel(
    private val api: NutritionApi,
    private val coachId: String,
    private val athleteId: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CoachNutritionUiState())
    val uiState = _uiState.asStateFlow()

    fun updateProtein(value: String) = updateField { copy(protein = value, message = null) }
    fun updateCarbohydrates(value: String) = updateField { copy(carbohydrates = value, message = null) }
    fun updateFats(value: String) = updateField { copy(fats = value, message = null) }
    fun updateCalories(value: String) = updateField { copy(calories = value, message = null) }

    fun saveTargets() {
        val state = _uiState.value
        val protein = state.protein.toDoubleOrNull()
        val carbs = state.carbohydrates.toDoubleOrNull()
        val fats = state.fats.toDoubleOrNull()
        val calories = state.calories.toDoubleOrNull()
        if (listOf(protein, carbs, fats, calories).any { it == null || it < 0.0 }) {
            _uiState.update { it.copy(message = "Enter a valid non-negative amount for each target.", isError = true) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, message = null, isError = false) }
            try {
                api.assignTargets(
                    AssignTargetsRequest(
                        coachId = coachId,
                        athleteId = athleteId,
                        proteinTarget = protein!!,
                        carbTarget = carbs!!,
                        fatTarget = fats!!,
                        calorieTarget = calories!!,
                    ),
                )
                _uiState.update { it.copy(isSaving = false, message = "Daily targets saved.", isError = false) }
            } catch (error: Exception) {
                _uiState.update { it.copy(isSaving = false, message = error.message ?: "Could not save targets.", isError = true) }
            }
        }
    }

    private fun updateField(transform: CoachNutritionUiState.() -> CoachNutritionUiState) =
        _uiState.update(transform)
}
