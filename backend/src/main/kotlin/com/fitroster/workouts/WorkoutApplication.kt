package com.fitroster.workouts

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WorkoutApplication

fun main(args: Array<String>) {
    runApplication<WorkoutApplication>(*args)
}
