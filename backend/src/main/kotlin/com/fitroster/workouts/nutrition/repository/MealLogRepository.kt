package com.fitroster.workouts.nutrition.repository

import com.fitroster.workouts.nutrition.domain.MealLog
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.UUID

interface MealLogRepository : JpaRepository<MealLog, UUID> {
    fun findAllByAthleteIdAndLoggedAtGreaterThanEqualAndLoggedAtLessThanOrderByLoggedAtAsc(
        athleteId: UUID,
        start: LocalDateTime,
        end: LocalDateTime,
    ): List<MealLog>
}
