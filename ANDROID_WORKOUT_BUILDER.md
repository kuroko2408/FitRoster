# Android Workout Builder

This is a focused native Android app module in `FitRoster/app` using Kotlin, Jetpack Compose Material 3, MVVM, Retrofit, and coroutines.

## Configure

- Set `API_BASE_URL` in `app/build.gradle.kts`; the default `http://10.0.2.2:8080/` reaches a backend running on the host machine from the Android emulator.
- Replace `SAMPLE_COACH_ID` in `MainActivity.kt` with the signed-in coach's UUID. The sample UUID is only a wiring placeholder and will not satisfy a real coach foreign key.
- The backend must be reachable and must accept cleartext HTTP for the emulator default. Use HTTPS and remove `usesCleartextTraffic` for production.

## API calls

The Retrofit interface matches the workout API routes: create workout, add sets, and patch each set's target weight/RPE. The builder's set rows can be reordered locally with a long press on the drag handle. The current workout API has no set-reorder endpoint, so reordering already-saved sets is not persisted remotely; newly submitted sets use their current order.

The exercise shown in this initial builder is a visual starter row for barbell squats. It is intentionally limited to Workout Builder scope; exercise selection/catalog loading is not included.
