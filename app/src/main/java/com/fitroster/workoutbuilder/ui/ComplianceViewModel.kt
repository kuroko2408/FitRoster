package com.fitroster.workoutbuilder.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitroster.workoutbuilder.data.NutritionApi
import com.fitroster.workoutbuilder.data.NutritionSummaryDto
import com.fitroster.workoutbuilder.data.WorkoutApi
import com.fitroster.workoutbuilder.data.WorkoutDto
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.coroutineScope

data class ComplianceUiState(
    val workouts: List<WorkoutDto> = emptyList(),
    val nutrition: NutritionSummaryDto? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

class ComplianceViewModel(
    private val workoutApi: WorkoutApi,
    private val nutritionApi: NutritionApi,
    private val athleteId: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ComplianceUiState())
    val uiState = _uiState.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val (workouts, nutrition) = coroutineScope {
                    val workoutsTask = async { workoutApi.getAthleteWorkouts(athleteId) }
                    val nutritionTask = async { nutritionApi.getDailySummary(athleteId) }
                    workoutsTask.await() to nutritionTask.await()
                }
                _uiState.update {
                    it.copy(workouts = workouts, nutrition = nutrition, isLoading = false, errorMessage = null)
                }
            } catch (error: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message ?: "Could not load compliance data.") }
            }
        }
    }
}
