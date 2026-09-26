# FitRoster workout API

Focused Spring Boot/Kotlin API for workout creation and set target editing. Run with Java 21 and Gradle, providing the usual Spring datasource settings, for example `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and `SPRING_DATASOURCE_PASSWORD`.

## Routes

- `POST /api/workouts` — create a workout.
- `GET /api/workouts/{workoutId}` — retrieve a workout and its ordered sets.
- `GET /api/workouts/athlete/{athleteId}` — retrieve workouts assigned to an athlete, including set targets and logged results.
- `POST /api/workouts/{workoutId}/sets` — add one or more exercise sets.
- `PUT /api/workouts/{workoutId}/sets/reorder` — persist set order from a JSON array of set UUIDs.
- `PATCH /api/workouts/{workoutId}/sets/{setId}/targets` — update target weight and/or RPE.
- `PUT /api/workouts/{workoutId}/sets/{setId}/log` — save actual reps and weight and mark the set complete.
- `POST /api/nutrition/targets` — create or update an athlete's macro targets.
- `POST /api/nutrition/meals` — log a meal for an athlete.
- `GET /api/nutrition/athlete/{athleteId}/summary` — return today's consumed macro totals and targets. An optional `date=YYYY-MM-DD` query parameter selects another day.

## Database schema

The PostgreSQL DDL is in `src/main/resources/schema.sql`. Apply it to the target database before starting the API. Hibernate schema generation is not enabled. The JPA mappings reflect the `users`, `workouts`, and `sets` tables defined there.
