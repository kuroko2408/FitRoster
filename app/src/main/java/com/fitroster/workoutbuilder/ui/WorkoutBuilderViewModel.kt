package com.fitroster.workoutbuilder.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitroster.workoutbuilder.data.AddSetsRequest
import com.fitroster.workoutbuilder.data.CreateWorkoutRequest
import com.fitroster.workoutbuilder.data.SetInput
import com.fitroster.workoutbuilder.data.UpdateTargetsRequest
import com.fitroster.workoutbuilder.data.WorkoutApi
import com.fitroster.workoutbuilder.data.WorkoutSetDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class BuilderSet(
    val id: String = UUID.randomUUID().toString(),
    val exerciseName: String = "Barbell squat",
    val setNumber: Int = 1,
    val reps: String = "8",
    val weight: String = "60",
    val rpe: String = "7.5",
    val serverId: String? = null,
)

data class WorkoutBuilderUiState(
    val workoutName: String = "Lower body strength",
    val sets: List<BuilderSet> = listOf(
        BuilderSet(setNumber = 1), BuilderSet(setNumber = 2), BuilderSet(setNumber = 3),
    ),
    val workoutId: String? = null,
    val isSaving: Boolean = false,
    val message: String? = null,
)

class WorkoutBuilderViewModel(
    private val api: WorkoutApi,
    private val coachId: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow(WorkoutBuilderUiState())
    val uiState = _uiState.asStateFlow()

    fun updateName(value: String) = _uiState.update { it.copy(workoutName = value) }
    fun updateSet(id: String, reps: String? = null, weight: String? = null, rpe: String? = null) = _uiState.update { state ->
        state.copy(sets = state.sets.map { row -> if (row.id == id) row.copy(reps = reps ?: row.reps, weight = weight ?: row.weight, rpe = rpe ?: row.rpe) else row })
    }
    fun addSet() = _uiState.update { state ->
        val last = state.sets.lastOrNull() ?: BuilderSet()
        state.copy(sets = state.sets + last.copy(id = UUID.randomUUID().toString(), setNumber = state.sets.size + 1, serverId = null), message = null)
    }
    fun removeSet(id: String) = _uiState.update { state ->
        state.copy(sets = state.sets.filterNot { it.id == id }.mapIndexed { index, row -> row.copy(setNumber = index + 1) })
    }
    fun reorder(from: Int, to: Int) = _uiState.update { state ->
        if (from !in state.sets.indices || to !in state.sets.indices || from == to) state else {
            val rows = state.sets.toMutableList().apply { add(to, removeAt(from)) }
            state.copy(sets = rows.mapIndexed { index, row -> row.copy(setNumber = index + 1) })
        }
    }

    fun save() {
        if (_uiState.value.isSaving) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, message = null) }
            try {
                var workoutId = _uiState.value.workoutId
                if (workoutId == null) {
                    val created = api.createWorkout(CreateWorkoutRequest(coachId = coachId, name = _uiState.value.workoutName.trim()))
                    workoutId = created.id
                    _uiState.update { it.copy(workoutId = workoutId) }
                }
                val state = _uiState.value
                val pending = state.sets.filter { it.serverId == null }
                if (pending.isNotEmpty()) {
                    val result = api.addSets(workoutId!!, AddSetsRequest(pending.mapIndexed { index, row ->
                        SetInput(row.exerciseName, index, row.setNumber, row.reps.toIntOrNull(), row.weight.toDoubleOrNull(), row.rpe.toDoubleOrNull())
                    }))
                    applyServerSets(result.sets)
                }
                val current = _uiState.value
                current.sets.filter { it.serverId != null }.forEach { row ->
                    api.updateTargets(workoutId!!, row.serverId!!, UpdateTargetsRequest(row.weight.toDoubleOrNull(), row.rpe.toDoubleOrNull()))
                }
                _uiState.update { it.copy(isSaving = false, message = "Workout saved") }
            } catch (error: Exception) {
                _uiState.update { it.copy(isSaving = false, message = error.message ?: "Could not save workout") }
            }
        }
    }

    private fun applyServerSets(rows: List<WorkoutSetDto>) = _uiState.update { state ->
        var available = rows.toMutableList()
        state.copy(sets = state.sets.map { row ->
            if (row.serverId != null) row else {
                val match = available.firstOrNull { it.setNumber == row.setNumber && it.exerciseName == row.exerciseName }
                    ?: available.firstOrNull()
                if (match == null) row else {
                    available.remove(match)
                    row.copy(serverId = match.id)
                }
            }
        })
    }
}
