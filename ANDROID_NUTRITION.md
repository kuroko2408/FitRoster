# Android Nutrition & Macro Tracking

The nutrition UI lives in `app/src/main/java/com/fitroster/workoutbuilder/ui` and uses the Retrofit endpoints in `data/NutritionApi.kt`.

- Coaches can switch between Workout Builder and Macros to create or update an athlete's daily targets.
- Athletes can switch between Gym Logger and Food & macros to view their daily summary and log meals. The summary refreshes after a successful meal submission.
- `MainActivity.kt` contains placeholder coach and athlete UUIDs. Replace them with IDs for real users in the backend before API calls can succeed.
- The API URL is configured by `API_BASE_URL` in `app/build.gradle.kts`.
