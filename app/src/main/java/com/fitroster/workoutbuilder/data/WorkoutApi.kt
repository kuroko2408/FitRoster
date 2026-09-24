package com.fitroster.workoutbuilder.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

data class CreateWorkoutRequest(val coachId: String, val athleteId: String? = null, val name: String, val description: String? = null)
data class AddSetsRequest(val sets: List<SetInput>)
data class SetInput(
    val exerciseName: String,
    val position: Int,
    val setNumber: Int,
    val targetReps: Int?,
    val targetWeight: Double?,
    val targetRpe: Double?,
    val tempo: String? = null,
    val restSeconds: Int? = null,
)
data class UpdateTargetsRequest(val targetWeight: Double? = null, val targetRpe: Double? = null)
data class WorkoutDto(
    val id: String,
    val coachId: String,
    val athleteId: String?,
    val name: String,
    val description: String?,
    val scheduledDate: String? = null,
    val createdAt: String? = null,
    val sets: List<WorkoutSetDto> = emptyList(),
)
data class WorkoutSetDto(
    val id: String,
    val exerciseName: String,
    val position: Int,
    val setNumber: Int,
    val targetReps: Int?,
    val targetWeight: Double?,
    val targetRpe: Double?,
    val tempo: String?,
    val restSeconds: Int?,
    val actualReps: Int? = null,
    val actualWeight: Double? = null,
    val isCompleted: Boolean = false,
)

interface WorkoutApi {
    @GET("api/workouts/athlete/{athleteId}")
    suspend fun getAthleteWorkouts(@Path("athleteId") athleteId: String): List<WorkoutDto>

    @POST("api/workouts") suspend fun createWorkout(@Body request: CreateWorkoutRequest): WorkoutDto
    @POST("api/workouts/{workoutId}/sets") suspend fun addSets(@Path("workoutId") workoutId: String, @Body request: AddSetsRequest): WorkoutDto
    @PATCH("api/workouts/{workoutId}/sets/{setId}/targets")
    suspend fun updateTargets(@Path("workoutId") workoutId: String, @Path("setId") setId: String, @Body request: UpdateTargetsRequest): WorkoutSetDto

    companion object {
        fun create(baseUrl: String): WorkoutApi = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WorkoutApi::class.java)
    }
}
