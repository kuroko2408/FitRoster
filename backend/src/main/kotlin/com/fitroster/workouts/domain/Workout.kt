package com.fitroster.workouts.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GenerationType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "workouts")
class Workout(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    var id: UUID = UUID.randomUUID(),

    @Column(name = "coach_id")
    var coachId: UUID? = null,

    @Column(name = "athlete_id")
    var athleteId: UUID? = null,

    @Column(name = "title", nullable = false, length = 255)
    var title: String,

    @Column(name = "scheduled_date")
    var scheduledDate: LocalDate? = null,

    @Column(name = "created_at", updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "workout", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var sets: MutableList<WorkoutSet> = mutableListOf(),
)
