package com.fitroster.workouts.api

import com.fitroster.workouts.domain.Workout
import com.fitroster.workouts.domain.WorkoutSet
import com.fitroster.workouts.repository.WorkoutRepository
import com.fitroster.workouts.repository.WorkoutSetRepository
import com.fitroster.workouts.service.WorkoutSetService
import com.fitroster.workouts.service.AthleteWorkoutService
import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@RestController
@RequestMapping("/api/workouts")
class WorkoutController(
    private val workouts: WorkoutRepository,
    private val sets: WorkoutSetRepository,
    private val workoutSetService: WorkoutSetService,
    private val athleteWorkoutService: AthleteWorkoutService,
) {
    @PostMapping
    @Transactional
    fun createWorkout(@Valid @RequestBody request: CreateWorkoutRequest): WorkoutResponse {
        return workouts.save(
            Workout(
                coachId = request.coachId,
                athleteId = request.athleteId,
                title = request.name.trim(),
            ),
        ).toResponse(emptyList())
    }

    @GetMapping("/{workoutId}")
    @Transactional(readOnly = true)
    fun getWorkout(@PathVariable workoutId: UUID): WorkoutResponse {
        val workout = findWorkout(workoutId)
        return workout.toResponse(sets.findAllByWorkoutIdOrderByDisplayOrderAsc(workoutId))
    }

    @GetMapping("/athlete/{athleteId}")
    @Transactional(readOnly = true)
    fun getAthleteAgenda(@PathVariable athleteId: UUID): List<WorkoutResponse> =
        athleteWorkoutService.getAthleteWorkouts(athleteId).map { workout ->
            workout.toResponse(workout.sets.sortedBy { it.displayOrder })
        }

    @PostMapping("/{workoutId}/sets")
    @Transactional
    fun addSets(
        @PathVariable workoutId: UUID,
        @Valid @RequestBody request: AddSetsRequest,
    ): WorkoutResponse {
        val workout = findWorkout(workoutId)
        val saved = sets.saveAll(request.sets.map { input ->
            WorkoutSet(
                workout = workout,
                exerciseName = input.exerciseName.trim(),
                displayOrder = input.position,
                reps = input.targetReps,
                targetWeight = input.targetWeight,
                targetRpe = input.targetRpe,
            )
        })
        return workout.toResponse(saved)
    }

    @PutMapping("/{workoutId}/sets/reorder")
    fun reorderSets(
        @PathVariable workoutId: UUID,
        @RequestBody orderedSetIds: List<UUID>,
    ): List<WorkoutSetResponse> {
        findWorkout(workoutId)
        return workoutSetService.reorderSets(workoutId, orderedSetIds).map { it.toResponse() }
    }

    @PatchMapping("/{workoutId}/sets/{setId}/targets")
    @Transactional
    fun updateTargets(
        @PathVariable workoutId: UUID,
        @PathVariable setId: UUID,
        @Valid @RequestBody request: UpdateTargetsRequest,
    ): WorkoutSetResponse {
        val set = sets.findById(setId).orElseThrow { notFound("Set", setId) }
        if (set.workout.id != workoutId) throw notFound("Set", setId)
        request.targetWeight?.let { set.targetWeight = it }
        request.targetRpe?.let { set.targetRpe = it }
        val saved = sets.save(set)
        return saved.toResponse()
    }

    @PutMapping("/{workoutId}/sets/{setId}/log")
    fun logCompletedSet(
        @PathVariable workoutId: UUID,
        @PathVariable setId: UUID,
        @Valid @RequestBody request: LogSetRequest,
    ): WorkoutSetResponse = athleteWorkoutService.logCompletedSet(
        workoutId = workoutId,
        setId = setId,
        actualReps = request.actualReps,
        actualWeight = request.actualWeight,
    ).toResponse()

    private fun findWorkout(id: UUID) = workouts.findById(id).orElseThrow { notFound("Workout", id) }

    private fun notFound(kind: String, id: UUID) =
        ResponseStatusException(HttpStatus.NOT_FOUND, "$kind $id was not found")
}

data class CreateWorkoutRequest(
    @field:NotNull val coachId: UUID,
    val athleteId: UUID? = null,
    @field:NotBlank @field:Size(max = 255) val name: String,
    val description: String? = null,
)

data class AddSetsRequest(
    @field:NotNull @field:Size(min = 1, max = 100) val sets: List<@Valid AddSetRequest>,
)

data class AddSetRequest(
    @field:NotBlank @field:Size(max = 255) val exerciseName: String,
    @field:Min(0) val position: Int,
    @field:Min(1) val setNumber: Int,
    @field:Min(1) val targetReps: Int,
    @field:DecimalMin("0.00") val targetWeight: BigDecimal? = null,
    @field:Min(0) @field:DecimalMax("10") val targetRpe: Int? = null,
    @field:Size(max = 32) val tempo: String? = null,
    @field:Min(0) val restSeconds: Int? = null,
)

data class UpdateTargetsRequest(
    @field:DecimalMin("0.00") val targetWeight: BigDecimal? = null,
    @field:Min(0) @field:DecimalMax("10") val targetRpe: Int? = null,
)

data class LogSetRequest(
    @field:Min(0) val actualReps: Int,
    @field:DecimalMin("0.00") val actualWeight: BigDecimal,
)

data class WorkoutResponse(
    val id: UUID,
    val coachId: UUID?,
    val athleteId: UUID?,
    val name: String,
    val description: String? = null,
    val createdAt: LocalDateTime,
    val sets: List<WorkoutSetResponse>,
)

data class WorkoutSetResponse(
    val id: UUID,
    val exerciseName: String,
    val position: Int,
    val setNumber: Int,
    val targetReps: Int,
    val targetWeight: BigDecimal?,
    val targetRpe: Int?,
    val actualReps: Int?,
    val actualWeight: BigDecimal?,
    val isCompleted: Boolean,
)

private fun Workout.toResponse(setRows: List<WorkoutSet>) = WorkoutResponse(
    id, coachId, athleteId, title, null, createdAt, setRows.sortedBy { it.displayOrder }.map { it.toResponse() },
)

private fun WorkoutSet.toResponse() = WorkoutSetResponse(
    id, exerciseName, displayOrder, displayOrder + 1, reps, targetWeight, targetRpe,
    actualReps, actualWeight, isCompleted,
)
