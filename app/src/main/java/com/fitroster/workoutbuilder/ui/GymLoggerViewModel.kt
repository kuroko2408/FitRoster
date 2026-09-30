package com.fitroster.workoutbuilder.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitroster.workoutbuilder.data.WorkoutApi
import com.fitroster.workoutbuilder.data.WorkoutDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
    val targetWeightKg: Double,
    val restSeconds: Int = 90,
    val isComplete: Boolean = false,
)

data class LoggerWorkout(
    val id: String,
    val title: String,
    val scheduledDate: String?,
    val exercises: List<LoggerExercise>,
)

data class GymLoggerUiState(
    val workouts: List<LoggerWorkout> = emptyList(),
    val activeSetIndex: Int = 0,
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val timerToken: Int = 0,
    val timerDurationSeconds: Int = 90,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)

class GymLoggerViewModel(
    private val api: WorkoutApi,
    private val athleteId: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow(GymLoggerUiState())
    val uiState = _uiState.asStateFlow()

    init { refreshWorkouts() }

    fun refreshWorkouts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val workouts = api.getAthleteWorkouts(athleteId).map(::toLoggerWorkout)
                val flatSets = workouts.flatMap { workout -> workout.exercises.flatMap { it.sets } }
                _uiState.update { state ->
                    state.copy(
                        workouts = workouts,
                        activeSetIndex = flatSets.indexOfFirst { !it.isComplete }.let { if (it < 0) flatSets.size else it },
                        completedCount = flatSets.count { it.isComplete },
                        totalCount = flatSets.size,
                        isLoading = false,
                        errorMessage = null,
                    )
                }
            } catch (error: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message ?: "Could not load assigned workouts.") }
            }
        }
    }

    fun completeSet(setId: String) {
        _uiState.update { state ->
            var restDuration = state.timerDurationSeconds
            val workouts = state.workouts.map { workout ->
                workout.copy(exercises = workout.exercises.map { exercise ->
                    exercise.copy(sets = exercise.sets.map { set ->
                        if (set.id == setId) {
                            restDuration = set.restSeconds
                            set.copy(isComplete = !set.isComplete)
                        } else set
                    })
                })
            }
            val flatSets = workouts.flatMap { workout -> workout.exercises.flatMap { exercise -> exercise.sets } }
            val wasCompleted = state.workouts.flatMap { workout -> workout.exercises.flatMap { exercise -> exercise.sets } }
                .firstOrNull { it.id == setId }?.isComplete == true
            state.copy(
                workouts = workouts,
                activeSetIndex = flatSets.indexOfFirst { !it.isComplete }.let { if (it < 0) flatSets.size else it },
                completedCount = flatSets.count { it.isComplete },
                timerToken = if (wasCompleted) state.timerToken else state.timerToken + 1,
                timerDurationSeconds = if (wasCompleted) state.timerDurationSeconds else restDuration,
            )
        }
    }
}

private fun toLoggerWorkout(workout: WorkoutDto): LoggerWorkout {
    val exercises = workout.sets
        .sortedBy { it.position }
        .groupBy { it.exerciseName }
        .map { (exerciseName, sets) ->
            LoggerExercise(
                id = "${workout.id}-$exerciseName",
                name = exerciseName,
                muscleGroup = "",
                sets = sets.sortedBy { it.position }.map { set ->
                    LoggerSet(
                        id = set.id,
                        number = set.setNumber,
                        reps = set.targetReps,
                        targetWeightKg = set.targetWeight ?: 0.0,
                        isComplete = set.isCompleted,
                    )
                },
            )
        }
    return LoggerWorkout(workout.id, workout.name, workout.createdAt?.take(10), exercises)
}
