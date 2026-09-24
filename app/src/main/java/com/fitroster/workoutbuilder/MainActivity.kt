package com.fitroster.workoutbuilder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fitroster.workoutbuilder.data.WorkoutApi
import com.fitroster.workoutbuilder.data.NutritionApi
import com.fitroster.workoutbuilder.navigation.FitRosterNavHost
import com.fitroster.workoutbuilder.ui.WorkoutBuilderViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val api = WorkoutApi.create(BuildConfig.API_BASE_URL)
        val nutritionApi = NutritionApi.create(BuildConfig.API_BASE_URL)
        val factory = WorkoutBuilderViewModelFactory(api, SAMPLE_COACH_ID)
        setContent {
            FitRosterTheme {
                val workoutBuilderViewModel: WorkoutBuilderViewModel = viewModel(factory = factory)
                FitRosterNavHost(
                    workoutBuilderViewModel = workoutBuilderViewModel,
                    workoutApi = api,
                    nutritionApi = nutritionApi,
                    coachId = SAMPLE_COACH_ID,
                    athleteId = SAMPLE_ATHLETE_ID,
                )
            }
        }
    }

    private companion object {
        // Replace with the authenticated coach's ID when authentication is connected.
        const val SAMPLE_COACH_ID = "00000000-0000-0000-0000-000000000001"
        const val SAMPLE_ATHLETE_ID = "00000000-0000-0000-0000-000000000002"
    }
}

private class WorkoutBuilderViewModelFactory(
    private val api: WorkoutApi,
    private val coachId: String,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(WorkoutBuilderViewModel::class.java))
        return WorkoutBuilderViewModel(api, coachId) as T
    }
}

@Composable
private fun FitRosterTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = androidx.compose.ui.graphics.Color(0xFF2E7D57),
            onPrimary = androidx.compose.ui.graphics.Color.White,
            background = androidx.compose.ui.graphics.Color(0xFFF5F7F4),
            surface = androidx.compose.ui.graphics.Color.White,
        ),
        content = content,
    )
}
