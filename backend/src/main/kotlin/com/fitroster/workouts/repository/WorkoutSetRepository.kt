package com.fitroster.workouts.repository

import com.fitroster.workouts.domain.WorkoutSet
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface WorkoutSetRepository : JpaRepository<WorkoutSet, UUID> {
    fun findAllByWorkoutIdOrderByDisplayOrderAsc(workoutId: UUID): List<WorkoutSet>
    fun findAllByWorkoutIdAndIdIn(workoutId: UUID, ids: Collection<UUID>): List<WorkoutSet>
    fun findByIdAndWorkout_Id(id: UUID, workoutId: UUID): WorkoutSet?
}
