package com.fitroster.workouts.nutrition.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "macro_targets")
class MacroTarget(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    var id: UUID = UUID.randomUUID(),

    @Column(name = "coach_id", nullable = false)
    var coachId: UUID,

    @Column(name = "athlete_id", nullable = false, unique = true)
    var athleteId: UUID,

    @Column(name = "protein_target", nullable = false, precision = 8, scale = 2)
    var proteinTarget: BigDecimal,

    @Column(name = "carb_target", nullable = false, precision = 8, scale = 2)
    var carbTarget: BigDecimal,

    @Column(name = "fat_target", nullable = false, precision = 8, scale = 2)
    var fatTarget: BigDecimal,

    @Column(name = "calorie_target", nullable = false, precision = 8, scale = 2)
    var calorieTarget: BigDecimal,
)
