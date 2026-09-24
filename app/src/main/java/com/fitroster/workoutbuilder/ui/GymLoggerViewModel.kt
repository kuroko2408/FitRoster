package com.fitroster.workoutbuilder.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoggerExercise(
    val id: String,
    val name: String,
    val muscleGroup: String,
    val sets: List<LoggerSet>,
)

data class LoggerSet(
    val id: String,
    val number: Int,
    val reps: Int,
    val targetWeightKg: Int,
    val restSeconds: Int = 90,
    val isComplete: Boolean = false,
)

data class GymLoggerUiState(
    val workoutTitle: String = "Lower Body Strength",
    val exercises: List<LoggerExercise> = emptyList(),
    val activeSetIndex: Int = 0,
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val timerToken: Int = 0,
    val timerDurationSeconds: Int = 90,
)

class GymLoggerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GymLoggerUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadMockWorkout()
    }

    fun loadMockWorkout() {
        val exercises = listOf(
            LoggerExercise(
                id = "barbell-squat",
                name = "Barbell squat",
                muscleGroup = "QUADRICEPS · GLUTES",
                sets = listOf(
                    LoggerSet("squat-1", 1, 8, 60),
                    LoggerSet("squat-2", 2, 8, 60),
                    LoggerSet("squat-3", 3, 8, 60),
                ),
            ),
            LoggerExercise(
                id = "romanian-deadlift",
                name = "Romanian deadlift",
                muscleGroup = "HAMSTRINGS · GLUTES",
                sets = listOf(
                    LoggerSet("rdl-1", 1, 10, 45),
                    LoggerSet("rdl-2", 2, 10, 45),
                    LoggerSet("rdl-3", 3, 10, 45),
                ),
            ),
            LoggerExercise(
                id = "walking-lunge",
                name = "Walking lunge",
                muscleGroup = "QUADRICEPS · GLUTES",
                sets = listOf(
                    LoggerSet("lunge-1", 1, 12, 12),
                    LoggerSet("lunge-2", 2, 12, 12),
                ),
            ),
        )
        val total = exercises.sumOf { it.sets.size }
        _uiState.value = GymLoggerUiState(
            exercises = exercises,
            activeSetIndex = 0,
            completedCount = 0,
            totalCount = total,
        )
    }

    fun completeSet(setId: String) {
        _uiState.update { state ->
            var restDuration = state.timerDurationSeconds
            val updatedExercises = state.exercises.map { exercise ->
                exercise.copy(sets = exercise.sets.map { set ->
                    if (set.id == setId) {
                        restDuration = set.restSeconds
                        set.copy(isComplete = !set.isComplete)
                    } else set
                })
            }
            val updatedSets = updatedExercises.flatMap { it.sets }
            val completed = updatedSets.count { it.isComplete }
            val nextActive = updatedSets.indexOfFirst { !it.isComplete }.let { if (it < 0) updatedSets.size else it }
            val wasCompleted = state.exercises.flatMap { it.sets }.firstOrNull { it.id == setId }?.isComplete == true
            state.copy(
                exercises = updatedExercises,
                activeSetIndex = nextActive,
                completedCount = completed,
                timerToken = if (wasCompleted) state.timerToken else state.timerToken + 1,
                timerDurationSeconds = if (wasCompleted) state.timerDurationSeconds else restDuration,
            )
        }
    }
}
