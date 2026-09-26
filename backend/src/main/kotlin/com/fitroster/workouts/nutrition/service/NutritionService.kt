package com.fitroster.workouts.nutrition.service

import com.fitroster.workouts.nutrition.domain.MacroTarget
import com.fitroster.workouts.nutrition.domain.MealLog
import com.fitroster.workouts.nutrition.repository.MacroTargetRepository
import com.fitroster.workouts.nutrition.repository.MealLogRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Service
class NutritionService(
    private val targets: MacroTargetRepository,
    private val meals: MealLogRepository,
) {
    @Transactional
    fun assignTargets(request: AssignMacroTargetsRequest): MacroTarget {
        val target = targets.findByAthleteId(request.athleteId)
        if (target == null) {
            return targets.save(
                MacroTarget(
                    coachId = request.coachId,
                    athleteId = request.athleteId,
                    proteinTarget = request.proteinTarget,
                    carbTarget = request.carbTarget,
                    fatTarget = request.fatTarget,
                    calorieTarget = request.calorieTarget,
                ),
            )
        }

        target.coachId = request.coachId
        target.proteinTarget = request.proteinTarget
        target.carbTarget = request.carbTarget
        target.fatTarget = request.fatTarget
        target.calorieTarget = request.calorieTarget
        return targets.save(target)
    }

    @Transactional
    fun logMeal(request: LogMealRequest): MealLog = meals.save(
        MealLog(
            athleteId = request.athleteId,
            foodName = request.foodName.trim(),
            protein = request.protein,
            carbs = request.carbs,
            fats = request.fats,
            calories = request.calories,
        ),
    )

    @Transactional(readOnly = true)
    fun getDailySummary(athleteId: UUID, date: LocalDate): NutritionSummaryResponse {
        val start = date.atStartOfDay()
        val end = date.plusDays(1).atStartOfDay()
        val dayMeals = meals.findAllByAthleteIdAndLoggedAtGreaterThanEqualAndLoggedAtLessThanOrderByLoggedAtAsc(
            athleteId = athleteId,
            start = start,
            end = end,
        )
        val consumed = MacroTotals(
            protein = dayMeals.sumOf { it.protein },
            carbs = dayMeals.sumOf { it.carbs },
            fats = dayMeals.sumOf { it.fats },
            calories = dayMeals.sumOf { it.calories },
        )
        val assigned = targets.findByAthleteId(athleteId)?.toResponse()
        return NutritionSummaryResponse(athleteId, date, consumed, assigned)
    }
}

data class AssignMacroTargetsRequest(
    val coachId: UUID,
    val athleteId: UUID,
    val proteinTarget: BigDecimal,
    val carbTarget: BigDecimal,
    val fatTarget: BigDecimal,
    val calorieTarget: BigDecimal,
)

data class LogMealRequest(
    val athleteId: UUID,
    val foodName: String,
    val protein: BigDecimal,
    val carbs: BigDecimal,
    val fats: BigDecimal,
    val calories: BigDecimal,
)

data class MacroTotals(
    val protein: BigDecimal,
    val carbs: BigDecimal,
    val fats: BigDecimal,
    val calories: BigDecimal,
)

data class MacroTargetResponse(
    val id: UUID,
    val coachId: UUID,
    val athleteId: UUID,
    val proteinTarget: BigDecimal,
    val carbTarget: BigDecimal,
    val fatTarget: BigDecimal,
    val calorieTarget: BigDecimal,
)

data class MealLogResponse(
    val id: UUID,
    val athleteId: UUID,
    val foodName: String,
    val protein: BigDecimal,
    val carbs: BigDecimal,
    val fats: BigDecimal,
    val calories: BigDecimal,
    val loggedAt: LocalDateTime,
)

data class NutritionSummaryResponse(
    val athleteId: UUID,
    val date: LocalDate,
    val consumed: MacroTotals,
    val targets: MacroTargetResponse?,
)

private fun MacroTarget.toResponse() = MacroTargetResponse(
    id, coachId, athleteId, proteinTarget, carbTarget, fatTarget, calorieTarget,
)

fun MealLog.toResponse() = MealLogResponse(
    id, athleteId, foodName, protein, carbs, fats, calories, loggedAt,
)
