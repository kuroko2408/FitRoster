package com.fitroster.workoutbuilder.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class AssignTargetsRequest(
    val coachId: String,
    val athleteId: String,
    val proteinTarget: Double,
    val carbTarget: Double,
    val fatTarget: Double,
    val calorieTarget: Double,
)

data class MacroTargetDto(
    val id: String,
    val coachId: String,
    val athleteId: String,
    val proteinTarget: Double,
    val carbTarget: Double,
    val fatTarget: Double,
    val calorieTarget: Double,
)

data class LogMealRequest(
    val athleteId: String,
    val foodName: String,
    val protein: Double,
    val carbs: Double,
    val fats: Double,
    val calories: Double,
)

data class MealLogDto(
    val id: String,
    val athleteId: String,
    val foodName: String,
    val protein: Double,
    val carbs: Double,
    val fats: Double,
    val calories: Double,
    val loggedAt: String,
)

data class MacroTotalsDto(
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fats: Double = 0.0,
    val calories: Double = 0.0,
)

data class NutritionSummaryDto(
    val athleteId: String,
    val date: String,
    val consumed: MacroTotalsDto,
    val targets: MacroTargetDto?,
)

interface NutritionApi {
    @POST("api/nutrition/targets")
    suspend fun assignTargets(@Body request: AssignTargetsRequest): MacroTargetDto

    @POST("api/nutrition/meals")
    suspend fun logMeal(@Body request: LogMealRequest): MealLogDto

    @GET("api/nutrition/athlete/{athleteId}/summary")
    suspend fun getDailySummary(@Path("athleteId") athleteId: String): NutritionSummaryDto

    companion object {
        fun create(baseUrl: String): NutritionApi = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NutritionApi::class.java)
    }
}
