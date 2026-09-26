package com.fitroster.workoutbuilder.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fitroster.workoutbuilder.data.WorkoutDto
import com.fitroster.workoutbuilder.data.WorkoutSetDto

private val ComplianceBg = Color(0xFFF4F7F4)
private val ComplianceInk = Color(0xFF17221D)
private val ComplianceMuted = Color(0xFF77827B)
private val ComplianceGreen = Color(0xFF2E7D57)
private val ComplianceMint = Color(0xFFE7F2EA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplianceDashboardScreen(viewModel: ComplianceViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val completedSets = state.workouts.sumOf { workout -> workout.sets.count { it.isCompleted } }
    val totalSets = state.workouts.sumOf { it.sets.size }
    val progress = if (totalSets == 0) 0f else completedSets.toFloat() / totalSets

    Scaffold(
        containerColor = ComplianceBg,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Athlete compliance", color = ComplianceInk, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                        Text("TRAINING & NUTRITION", color = ComplianceMuted, fontSize = 10.sp, letterSpacing = 1.5.sp)
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::refresh) {
                        Icon(Icons.Rounded.Refresh, contentDescription = "Refresh compliance", tint = ComplianceGreen)
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            item {
                Column {
                    Text("Weekly overview", color = ComplianceInk, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Text("A clear view of your athlete's daily habits", color = ComplianceMuted, fontSize = 13.sp, modifier = Modifier.padding(top = 5.dp))
                }
            }
            item {
                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.fillMaxWidth().padding(17.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(color = ComplianceMint, shape = RoundedCornerShape(11.dp)) {
                                Icon(Icons.Rounded.Restaurant, contentDescription = null, tint = ComplianceGreen, modifier = Modifier.padding(9.dp))
                            }
                            Column(Modifier.padding(start = 10.dp)) {
                                Text("Nutrition adherence", color = ComplianceInk, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(state.nutrition?.date ?: "Today's logged macros vs targets", color = ComplianceMuted, fontSize = 11.sp)
                            }
                            if (state.isLoading) {
                                Spacer(Modifier.weight(1f))
                                CircularProgressIndicator(Modifier.size(19.dp), color = ComplianceGreen, strokeWidth = 2.dp)
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        val consumed = state.nutrition?.consumed
                        val targets = state.nutrition?.targets
                        AdherenceMacroRow("Protein", consumed?.protein ?: 0.0, targets?.proteinTarget, "g", ComplianceGreen)
                        AdherenceMacroRow("Carbohydrates", consumed?.carbs ?: 0.0, targets?.carbTarget, "g", Color(0xFF477D9B))
                        AdherenceMacroRow("Fats", consumed?.fats ?: 0.0, targets?.fatTarget, "g", Color(0xFFC58338))
                        AdherenceMacroRow("Calories", consumed?.calories ?: 0.0, targets?.calorieTarget, "kcal", Color(0xFF8D6AB1))
                        if (!state.isLoading && state.nutrition != null && targets == null) {
                            Text("No macro targets have been assigned.", color = ComplianceMuted, fontSize = 12.sp)
                        }
                    }
                }
            }
            item {
                Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = ComplianceGreen)) {
                    Column(Modifier.fillMaxWidth().padding(17.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Workout execution", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Text("$completedSets / $totalSets sets", color = Color.White.copy(alpha = .9f), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth().padding(top = 13.dp).height(7.dp),
                            color = Color.White,
                            trackColor = Color.White.copy(alpha = .25f),
                        )
                    }
                }
            }
            if (state.errorMessage != null) {
                item {
                    Text(state.errorMessage!!, color = Color(0xFFB3261E), fontSize = 13.sp)
                }
            }
            if (state.workouts.isEmpty() && !state.isLoading) {
                item {
                    Surface(color = Color.White, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                        Text("No assigned workouts found.", color = ComplianceMuted, modifier = Modifier.padding(18.dp), fontSize = 13.sp)
                    }
                }
            }
            items(state.workouts, key = { it.id }) { workout ->
                WorkoutExecutionCard(workout)
            }
        }
    }
}

@Composable
private fun AdherenceMacroRow(label: String, consumed: Double, target: Double?, unit: String, tint: Color) {
    val ratio = if (target == null || target <= 0.0) 0f else (consumed / target).toFloat().coerceIn(0f, 1f)
    Column(Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(label, color = ComplianceInk, fontSize = 12.sp, modifier = Modifier.weight(1f))
            Text("${consumed.pretty()} / ${target?.pretty() ?: "—"} $unit", color = ComplianceInk, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
        LinearProgressIndicator(
            progress = { ratio },
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp).height(6.dp),
            color = tint,
            trackColor = Color(0xFFE9EEEA),
        )
    }
}

@Composable
private fun WorkoutExecutionCard(workout: WorkoutDto) {
    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(workout.name, color = ComplianceInk, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(workout.scheduledDate ?: workout.createdAt?.take(10) ?: "Scheduled workout", color = ComplianceMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 3.dp))
                }
                val completed = workout.sets.count { it.isCompleted }
                Text("$completed/${workout.sets.size}", color = ComplianceGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Spacer(Modifier.height(12.dp))
            workout.sets.forEach { set -> WorkoutSetExecutionRow(set) }
        }
    }
}

@Composable
private fun WorkoutSetExecutionRow(set: WorkoutSetDto) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(color = if (set.isCompleted) ComplianceMint else Color(0xFFF0F3F0), shape = CircleShape, modifier = Modifier.size(30.dp)) {
            if (set.isCompleted) {
                androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.CheckCircle, contentDescription = "Completed", tint = ComplianceGreen, modifier = Modifier.size(19.dp))
                }
            } else {
                androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                    Text("${set.setNumber}", color = ComplianceMuted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text("${set.exerciseName} · Set ${set.setNumber}", color = ComplianceInk, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Text("Target  ${set.targetReps ?: "—"} reps  ×  ${set.targetWeight?.pretty() ?: "—"} kg", color = ComplianceMuted, fontSize = 11.sp)
            if (set.isCompleted) {
                Text(
                    "Logged  ${set.actualReps ?: "—"} reps  ×  ${set.actualWeight?.pretty() ?: "—"} kg",
                    color = ComplianceGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
        if (set.isCompleted) {
            Text("DONE", color = ComplianceGreen, fontWeight = FontWeight.Bold, fontSize = 9.sp, letterSpacing = .6.sp)
        }
    }
}

private fun Double.pretty(): String = if (this % 1.0 == 0.0) toInt().toString() else "%.1f".format(this)
