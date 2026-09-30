package com.fitroster.workoutbuilder.data

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

data class CreateWorkoutRequest(
    val coachId: String,
    val athleteId: String,  // No '? = null' here anymore!
    val name: String,       // Spring Boot expects 'name', not 'title' in the JSON
    val description: String? = null
)
data class AddSetsRequest(val sets: List<SetInput>)
data class SetInput(
    val exerciseName: String,
    val position: Int,
    val setNumber: Int,
    val targetReps: Int,
    val targetWeight: Double?,
    val targetRpe: Int?,
    val tempo: String? = null,
    val restSeconds: Int? = null,
)
data class UpdateTargetsRequest(val targetWeight: Double? = null, val targetRpe: Int? = null)
data class WorkoutDto(
    val id: String,
    val coachId: String?,
    val athleteId: String?,
    val name: String,
    val description: String? = null,
    @SerializedName(value = "createdAt", alternate = ["created_at"])
    val createdAt: String? = null,
    @SerializedName("sets")
    val sets: List<WorkoutSetDto> = emptyList(),
)
data class WorkoutSetDto(
    val id: String,
    @SerializedName(value = "exerciseName", alternate = ["exercise_name"])
    val exerciseName: String,
    @SerializedName(value = "position", alternate = ["display_order"])
    val position: Int,
    @SerializedName(value = "setNumber", alternate = ["set_number"])
    val setNumber: Int,
    @SerializedName(value = "targetReps", alternate = ["target_reps", "reps"])
    val targetReps: Int,
    @SerializedName(value = "targetWeight", alternate = ["target_weight"])
    val targetWeight: Double?,
    @SerializedName(value = "targetRpe", alternate = ["target_rpe"])
    val targetRpe: Int?,
    @SerializedName(value = "actualReps", alternate = ["actual_reps"])
    val actualReps: Int? = null,
    @SerializedName(value = "actualWeight", alternate = ["actual_weight"])
    val actualWeight: Double? = null,
    @SerializedName(value = "isCompleted", alternate = ["is_completed"])
    val isCompleted: Boolean = false,
)

interface WorkoutApi {
    @GET("api/workouts/athlete/{athleteId}")
    suspend fun getAthleteWorkouts(@Path("athleteId") athleteId: String): List<WorkoutDto>

    @GET("api/workouts/{workoutId}")
    suspend fun getWorkout(@Path("workoutId") workoutId: String): WorkoutDto

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
