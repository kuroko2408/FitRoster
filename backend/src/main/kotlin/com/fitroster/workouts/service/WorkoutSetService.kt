package com.fitroster.workouts.service

import com.fitroster.workouts.domain.WorkoutSet
import com.fitroster.workouts.repository.WorkoutSetRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class WorkoutSetService(
    private val sets: WorkoutSetRepository,
) {
    @Transactional
    fun reorderSets(workoutId: UUID, orderedSetIds: List<UUID>): List<WorkoutSet> {
        if (orderedSetIds.size != orderedSetIds.distinct().size) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Set IDs must not contain duplicates")
        }
        if (orderedSetIds.isEmpty()) return emptyList()

        val setsById = sets.findAllByWorkoutIdAndIdIn(workoutId, orderedSetIds).associateBy { it.id }
        if (setsById.size != orderedSetIds.size) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "One or more sets do not belong to workout $workoutId")
        }

        val reordered = orderedSetIds.mapIndexed { index, setId ->
            setsById.getValue(setId).apply { displayOrder = index }
        }
        return sets.saveAll(reordered)
    }
}
