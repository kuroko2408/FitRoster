package com.fitroster.workouts.nutrition.api

import com.fitroster.workouts.nutrition.domain.MacroTarget
import com.fitroster.workouts.nutrition.domain.MealLog
import com.fitroster.workouts.nutrition.service.AssignMacroTargetsRequest
import com.fitroster.workouts.nutrition.service.LogMealRequest
import com.fitroster.workouts.nutrition.service.MacroTargetResponse
import com.fitroster.workouts.nutrition.service.MealLogResponse
import com.fitroster.workouts.nutrition.service.NutritionService
import com.fitroster.workouts.nutrition.service.NutritionSummaryResponse
import com.fitroster.workouts.nutrition.service.toResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@RestController
@RequestMapping("/api/nutrition")
class NutritionController(
    private val nutritionService: NutritionService,
) {
    @PostMapping("/targets")
    @ResponseStatus(HttpStatus.CREATED)
    fun assignTargets(@Valid @RequestBody request: AssignTargetsBody): MacroTargetResponse =
        nutritionService.assignTargets(request.toServiceRequest()).toResponse()

    @PostMapping("/meals")
    @ResponseStatus(HttpStatus.CREATED)
    fun logMeal(@Valid @RequestBody request: LogMealBody): MealLogResponse =
        nutritionService.logMeal(request.toServiceRequest()).toResponse()

    @GetMapping("/athlete/{athleteId}/summary")
    fun dailySummary(
        @PathVariable athleteId: UUID,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate?,
    ): NutritionSummaryResponse = nutritionService.getDailySummary(athleteId, date ?: LocalDate.now())
}

data class AssignTargetsBody(
    val coachId: UUID,
    val athleteId: UUID,
    @field:DecimalMin("0.00") val proteinTarget: BigDecimal,
    @field:DecimalMin("0.00") val carbTarget: BigDecimal,
    @field:DecimalMin("0.00") val fatTarget: BigDecimal,
    @field:DecimalMin("0.00") val calorieTarget: BigDecimal,
) {
    fun toServiceRequest() = AssignMacroTargetsRequest(
        coachId, athleteId, proteinTarget, carbTarget, fatTarget, calorieTarget,
    )
}

data class LogMealBody(
    val athleteId: UUID,
    @field:NotBlank @field:Size(max = 255) val foodName: String,
    @field:DecimalMin("0.00") val protein: BigDecimal,
    @field:DecimalMin("0.00") val carbs: BigDecimal,
    @field:DecimalMin("0.00") val fats: BigDecimal,
    @field:DecimalMin("0.00") val calories: BigDecimal,
) {
    fun toServiceRequest() = LogMealRequest(athleteId, foodName, protein, carbs, fats, calories)
}

private fun MacroTarget.toResponse() = MacroTargetResponse(
    id, coachId, athleteId, proteinTarget, carbTarget, fatTarget, calorieTarget,
)
