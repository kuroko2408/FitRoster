package com.fitroster.workoutbuilder.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fitroster.workoutbuilder.data.NutritionApi
import com.fitroster.workoutbuilder.data.WorkoutApi
import com.fitroster.workoutbuilder.ui.AthleteNutritionViewModel
import com.fitroster.workoutbuilder.ui.CoachNutritionViewModel
import com.fitroster.workoutbuilder.ui.ComplianceDashboardScreen
import com.fitroster.workoutbuilder.ui.ComplianceViewModel
import com.fitroster.workoutbuilder.ui.FoodLoggingScreen
import com.fitroster.workoutbuilder.ui.MacroAllocatorScreen
import com.fitroster.workoutbuilder.ui.GymLoggerScreen
import com.fitroster.workoutbuilder.ui.WorkoutBuilderScreen
import com.fitroster.workoutbuilder.ui.WorkoutBuilderViewModel

object AppRoute {
    const val RoleSelection = "RoleSelection"
    const val CoachDashboard = "CoachDashboard"
    const val AthleteDashboard = "AthleteDashboard"
}

@Composable
fun FitRosterNavHost(
    workoutBuilderViewModel: WorkoutBuilderViewModel,
    workoutApi: WorkoutApi,
    nutritionApi: NutritionApi,
    coachId: String,
    athleteId: String,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = AppRoute.RoleSelection) {
        composable(AppRoute.RoleSelection) {
            RoleSelectionScreen(
                onCoachSelected = { navController.navigateSingleTop(AppRoute.CoachDashboard) },
                onAthleteSelected = { navController.navigateSingleTop(AppRoute.AthleteDashboard) },
            )
        }
        composable(AppRoute.CoachDashboard) {
            var coachTab by rememberSaveable { mutableIntStateOf(0) }
            Column(Modifier.fillMaxSize()) {
                DashboardSwitch(listOf("Workout builder", "Macros", "Compliance"), coachTab) { coachTab = it }
                Box(Modifier.weight(1f)) {
                    when (coachTab) {
                        0 -> WorkoutBuilderScreen(workoutBuilderViewModel)
                        1 -> {
                            val vm: CoachNutritionViewModel = viewModel(factory = CoachNutritionFactory(nutritionApi, coachId, athleteId))
                            MacroAllocatorScreen(vm)
                        }
                        else -> {
                            val vm: ComplianceViewModel = viewModel(factory = ComplianceFactory(workoutApi, nutritionApi, athleteId))
                            ComplianceDashboardScreen(vm)
                        }
                    }
                }
            }
        }
        composable(AppRoute.AthleteDashboard) {
            var athleteTab by rememberSaveable { mutableIntStateOf(0) }
            Column(Modifier.fillMaxSize()) {
                DashboardSwitch(listOf("Workout", "Food & macros"), athleteTab) { athleteTab = it }
                Box(Modifier.weight(1f)) {
                    when (athleteTab) {
                        0 -> GymLoggerScreen()
                        else -> {
                            val vm: AthleteNutritionViewModel = viewModel(factory = AthleteNutritionFactory(nutritionApi, athleteId))
                            FoodLoggingScreen(vm)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardSwitch(labels: List<String>, selectedIndex: Int, onSelect: (Int) -> Unit) {
    Surface(color = Color.White, shadowElevation = 1.dp) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            labels.forEachIndexed { index, label ->
                val selected = selectedIndex == index
                Surface(
                    color = if (selected) Color(0xFFE7F2EA) else Color.Transparent,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).clickable { onSelect(index) },
                ) {
                    Text(
                        label,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                        color = if (selected) Color(0xFF2E7D57) else Color(0xFF77827B),
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                        fontSize = 13.sp,
                    )
                }
            }
        }
    }
}

private class ComplianceFactory(
    private val workoutApi: WorkoutApi,
    private val nutritionApi: NutritionApi,
    private val athleteId: String,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(ComplianceViewModel::class.java))
        return ComplianceViewModel(workoutApi, nutritionApi, athleteId) as T
    }
}

private class CoachNutritionFactory(
    private val api: NutritionApi,
    private val coachId: String,
    private val athleteId: String,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(CoachNutritionViewModel::class.java))
        return CoachNutritionViewModel(api, coachId, athleteId) as T
    }
}

private class AthleteNutritionFactory(
    private val api: NutritionApi,
    private val athleteId: String,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(AthleteNutritionViewModel::class.java))
        return AthleteNutritionViewModel(api, athleteId) as T
    }
}

private fun NavHostController.navigateSingleTop(route: String) {
    navigate(route) {
        popUpTo(AppRoute.RoleSelection) { inclusive = true }
        launchSingleTop = true
    }
}

@Composable
private fun RoleSelectionScreen(
    onCoachSelected: () -> Unit,
    onAthleteSelected: () -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF4F7F4)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 26.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(color = Color(0xFFE7F2EA), shape = RoundedCornerShape(22.dp), modifier = Modifier.size(76.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.FitnessCenter, contentDescription = null, tint = Color(0xFF2E7D57), modifier = Modifier.size(34.dp))
                }
            }
            Spacer(Modifier.height(22.dp))
            Text("FitRoster", color = Color(0xFF17221D), fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text("Training, together.", color = Color(0xFF77827B), fontSize = 15.sp, modifier = Modifier.padding(top = 5.dp))

            Surface(
                color = Color.White,
                shape = RoundedCornerShape(22.dp),
                shadowElevation = 3.dp,
                modifier = Modifier.fillMaxWidth().padding(top = 38.dp),
            ) {
                Column(Modifier.fillMaxWidth().padding(20.dp)) {
                    Text("Welcome back", color = Color(0xFF17221D), fontSize = 19.sp, fontWeight = FontWeight.Bold)
                    Text("Choose how you want to continue", color = Color(0xFF77827B), fontSize = 13.sp, modifier = Modifier.padding(top = 4.dp, bottom = 20.dp))
                    Button(
                        onClick = onCoachSelected,
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D57)),
                    ) {
                        Icon(Icons.Rounded.FitnessCenter, contentDescription = null)
                        Text("Log in as Coach", modifier = Modifier.padding(start = 9.dp), fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = onAthleteSelected,
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF0F4F1),
                            contentColor = Color(0xFF203129),
                        ),
                    ) {
                        Icon(Icons.Rounded.Person, contentDescription = null)
                        Text("Log in as Athlete", modifier = Modifier.padding(start = 9.dp), fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            Text("MOCK ROLE SELECTION", color = Color(0xFF9AA49D), fontSize = 10.sp, letterSpacing = 1.5.sp, modifier = Modifier.padding(top = 20.dp))
        }
    }
}
