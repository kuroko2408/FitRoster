package com.fitroster.workoutbuilder.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

private val LoggerBackground = Color(0xFFF4F7F4)
private val LoggerInk = Color(0xFF17221D)
private val LoggerMuted = Color(0xFF77827B)
private val LoggerGreen = Color(0xFF2E7D57)
private val LoggerMint = Color(0xFFE7F2EA)
private val TimerAlert = Color(0xFFFFF0E8)
private val TimerAlertInk = Color(0xFFB54725)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymLoggerScreen(viewModel: GymLoggerViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var remainingSeconds by rememberSaveable { mutableIntStateOf(0) }
    var savedTimerToken by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(state.timerToken) {
        if (state.timerToken > savedTimerToken) {
            savedTimerToken = state.timerToken
            remainingSeconds = state.timerDurationSeconds
        }
    }
    LaunchedEffect(remainingSeconds) {
        if (remainingSeconds > 0) {
            delay(1_000)
            remainingSeconds -= 1
        }
    }

    val timerFinished = savedTimerToken > 0 && remainingSeconds == 0
    val completedFraction = if (state.totalCount == 0) 0f else state.completedCount.toFloat() / state.totalCount

    Scaffold(
        containerColor = LoggerBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Today's session", color = LoggerInk, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("ATHLETE WORKOUT", color = LoggerMuted, fontSize = 10.sp, letterSpacing = 1.5.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {}) { Icon(Icons.Rounded.ArrowBack, contentDescription = "Back", tint = LoggerInk) }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(start = 18.dp, end = 18.dp, top = 8.dp, bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                Column {
                    Text("${state.completedCount} of ${state.totalCount} sets complete", color = LoggerMuted, fontSize = 13.sp)
                    Spacer(Modifier.height(9.dp))
                    LinearProgressIndicator(
                        progress = { completedFraction },
                        modifier = Modifier.fillMaxWidth().height(7.dp).clip(CircleShape),
                        color = LoggerGreen,
                        trackColor = Color(0xFFE0E7E1),
                    )
                    Text(state.workoutTitle, modifier = Modifier.padding(top = 19.dp), color = LoggerInk, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text("LOWER BODY · 45–55 MIN", modifier = Modifier.padding(top = 4.dp), color = LoggerMuted, fontSize = 10.sp, letterSpacing = 1.2.sp)
                }
            }

            if (savedTimerToken > 0) {
                item { RestTimerCard(remainingSeconds, state.timerDurationSeconds, timerFinished) }
            }

            var flattenedIndex = 0
            state.exercises.forEach { exercise ->
                val exerciseStartIndex = flattenedIndex
                flattenedIndex += exercise.sets.size
                item(key = "exercise-${exercise.id}") { ExerciseHeading(exercise) }
                itemsIndexed(exercise.sets, key = { _, set -> set.id }) { index, set ->
                    val absoluteIndex = exerciseStartIndex + index
                    SetChecklistRow(
                        set = set,
                        isActive = absoluteIndex == state.activeSetIndex,
                        onToggle = { viewModel.completeSet(set.id) },
                    )
                }
            }

            if (state.completedCount == state.totalCount && state.totalCount > 0) {
                item {
                    Surface(color = LoggerMint, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = LoggerGreen)
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text("Workout complete", color = LoggerInk, fontWeight = FontWeight.Bold)
                                Text("Great work. You finished every set.", color = LoggerMuted, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExerciseHeading(exercise: LoggerExercise) {
    Row(Modifier.fillMaxWidth().padding(top = 5.dp, bottom = 0.dp), verticalAlignment = Alignment.CenterVertically) {
        Surface(color = LoggerMint, shape = RoundedCornerShape(11.dp), modifier = Modifier.size(40.dp)) {
            Box(contentAlignment = Alignment.Center) { Icon(Icons.Rounded.FitnessCenter, null, tint = LoggerGreen, modifier = Modifier.size(19.dp)) }
        }
        Spacer(Modifier.width(11.dp))
        Column {
            Text(exercise.name, color = LoggerInk, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(exercise.muscleGroup, color = LoggerMuted, fontSize = 9.sp, letterSpacing = 1.sp)
        }
    }
}

@Composable
private fun SetChecklistRow(set: LoggerSet, isActive: Boolean, onToggle: () -> Unit) {
    val surfaceColor by animateColorAsState(
        targetValue = when {
            set.isComplete -> LoggerMint
            isActive -> Color.White
            else -> Color(0xFFFAFBFA)
        },
        label = "set-row-color",
    )
    Card(
        onClick = onToggle,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isActive && !set.isComplete) 2.dp else 0.dp),
    ) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 13.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = if (set.isComplete) LoggerGreen else Color(0xFFF0F3F0), shape = CircleShape, modifier = Modifier.size(34.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    if (set.isComplete) Icon(Icons.Rounded.Check, contentDescription = "Completed", tint = Color.White, modifier = Modifier.size(19.dp))
                    else Text("${set.number}", color = if (isActive) LoggerGreen else LoggerMuted, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("Set ${set.number}", color = LoggerInk, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text("${set.reps} reps", color = LoggerMuted, fontSize = 12.sp)
            }
            Surface(color = Color.White.copy(alpha = .8f), shape = RoundedCornerShape(10.dp)) {
                Text("${set.targetWeightKg} kg", color = LoggerInk, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp))
            }
            Spacer(Modifier.width(10.dp))
            Text(if (set.isComplete) "DONE" else "CHECK", color = if (set.isComplete) LoggerGreen else LoggerMuted, fontWeight = FontWeight.Bold, fontSize = 9.sp, letterSpacing = .6.sp)
        }
    }
}

@Composable
private fun RestTimerCard(remainingSeconds: Int, durationSeconds: Int, timerFinished: Boolean) {
    val background by animateColorAsState(if (timerFinished) TimerAlert else Color.White, label = "timer-background")
    val accent = if (timerFinished) TimerAlertInk else LoggerGreen
    Surface(color = background, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(52.dp)) {
                CircularProgressIndicator(
                    progress = { if (durationSeconds <= 0) 0f else remainingSeconds.toFloat() / durationSeconds },
                    modifier = Modifier.fillMaxSize(),
                    color = accent,
                    trackColor = accent.copy(alpha = .16f),
                    strokeWidth = 4.dp,
                )
                Text(formatTime(remainingSeconds), color = accent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(if (timerFinished) "Rest complete" else "Recovery break", color = if (timerFinished) TimerAlertInk else LoggerInk, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(if (timerFinished) "You're ready for the next set" else "Take a breath before your next set", color = LoggerMuted, fontSize = 12.sp)
            }
            if (timerFinished) {
                Icon(Icons.Rounded.NotificationsActive, contentDescription = "Rest timer finished", tint = TimerAlertInk)
            } else {
                Icon(Icons.Rounded.Pause, contentDescription = "Timer running", tint = LoggerMuted)
            }
        }
    }
}

private fun formatTime(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}
