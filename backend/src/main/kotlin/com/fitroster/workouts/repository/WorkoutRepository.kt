package com.fitroster.workouts.repository

import com.fitroster.workouts.domain.Workout
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface WorkoutRepository : JpaRepository<Workout, UUID> {
    @EntityGraph(attributePaths = ["sets"])
    fun findAllByAthleteIdOrderByScheduledDateAscCreatedAtAsc(athleteId: UUID): List<Workout>
}
