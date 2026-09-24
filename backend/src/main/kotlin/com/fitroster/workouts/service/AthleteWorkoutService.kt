package com.fitroster.workouts.service

import com.fitroster.workouts.domain.Workout
import com.fitroster.workouts.domain.WorkoutSet
import com.fitroster.workouts.repository.WorkoutRepository
import com.fitroster.workouts.repository.WorkoutSetRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.util.UUID

@Service
class AthleteWorkoutService(
    private val workouts: WorkoutRepository,
    private val sets: WorkoutSetRepository,
) {
    @Transactional(readOnly = true)
    fun getAthleteWorkouts(athleteId: UUID): List<Workout> =
        workouts.findAllByAthleteIdOrderByScheduledDateAscCreatedAtAsc(athleteId)

    @Transactional
    fun logCompletedSet(
        workoutId: UUID,
        setId: UUID,
        actualReps: Int,
        actualWeight: BigDecimal,
    ): WorkoutSet {
        val set = sets.findByIdAndWorkout_Id(setId, workoutId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Set $setId was not found in workout $workoutId")
        set.actualReps = actualReps
        set.actualWeight = actualWeight
        set.isCompleted = true
        return sets.save(set)
    }
}
