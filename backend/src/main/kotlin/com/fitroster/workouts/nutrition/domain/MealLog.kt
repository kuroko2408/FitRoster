package com.fitroster.workouts.nutrition.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "meal_logs")
class MealLog(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    var id: UUID = UUID.randomUUID(),

    @Column(name = "athlete_id", nullable = false)
    var athleteId: UUID,

    @Column(name = "food_name", nullable = false, length = 255)
    var foodName: String,

    @Column(name = "protein", nullable = false, precision = 8, scale = 2)
    var protein: BigDecimal,

    @Column(name = "carbs", nullable = false, precision = 8, scale = 2)
    var carbs: BigDecimal,

    @Column(name = "fats", nullable = false, precision = 8, scale = 2)
    var fats: BigDecimal,

    @Column(name = "calories", nullable = false, precision = 8, scale = 2)
    var calories: BigDecimal,

    @Column(name = "logged_at", nullable = false)
    var loggedAt: LocalDateTime = LocalDateTime.now(),
)
