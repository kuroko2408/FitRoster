package com.fitroster.workouts.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "sets")
class WorkoutSet(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    var id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id")
    var workout: Workout,

    @Column(name = "exercise_name", nullable = false, length = 255)
    var exerciseName: String,

    @Column(name = "reps", nullable = false)
    var reps: Int,

    @Column(name = "target_weight", precision = 5, scale = 2)
    var targetWeight: BigDecimal? = null,

    @Column(name = "target_rpe")
    var targetRpe: Int? = null,

    @Column(name = "display_order", nullable = false)
    var displayOrder: Int,

    @Column(name = "actual_reps")
    var actualReps: Int? = null,

    @Column(name = "actual_weight", precision = 5, scale = 2)
    var actualWeight: BigDecimal? = null,

    @Column(name = "is_completed", nullable = false)
    var isCompleted: Boolean = false,

    @Column(name = "created_at", updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),
)
