package com.fitroster.workouts.nutrition.repository

import com.fitroster.workouts.nutrition.domain.MacroTarget
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MacroTargetRepository : JpaRepository<MacroTarget, UUID> {
    fun findByAthleteId(athleteId: UUID): MacroTarget?
}
