package com.fitroster.workoutbuilder.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.DragIndicator
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

private val Ink = Color(0xFF17221D)
private val Muted = Color(0xFF78827C)
private val Canvas = Color(0xFFF5F7F4)
private val Green = Color(0xFF2E7D57)
private val Mint = Color(0xFFE8F3EC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutBuilderScreen(viewModel: WorkoutBuilderViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    Scaffold(
        containerColor = Canvas,
        topBar = {
            TopAppBar(
                title = { Column { Text("Workout builder", fontWeight = FontWeight.Bold, fontSize = 19.sp); Text("PROGRAM DESIGN", color = Muted, fontSize = 10.sp, letterSpacing = 1.6.sp) } },
                navigationIcon = { IconButton(onClick = {}) { Icon(Icons.Rounded.ArrowBack, "Back", tint = Ink) } },
                actions = { IconButton(onClick = {}) { Icon(Icons.Rounded.MoreHoriz, "More options", tint = Ink) } },
            )
        },
        bottomBar = {
            Surface(shadowElevation = 10.dp, color = Color.White) {
                Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)) {
                    Button(
                        onClick = viewModel::save,
                        enabled = !state.isSaving && state.sets.isNotEmpty() && state.workoutName.isNotBlank(),
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Green),
                    ) {
                        if (state.isSaving) CircularProgressIndicator(Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                        else { Icon(Icons.Rounded.Check, null); Spacer(Modifier.width(8.dp)); Text("Save workout", fontWeight = FontWeight.SemiBold) }
                    }
                    state.message?.let { Text(it, color = if (it == "Workout saved") Green else Color(0xFFB3261E), modifier = Modifier.padding(top = 6.dp)) }
                }
            }
        },
    ) { padding ->
        LazyColumn(
            state = listState,
            contentPadding = padding,
            modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                Column {
                    Spacer(Modifier.height(8.dp))
                    Text("Build a session", color = Muted, fontSize = 14.sp)
                    Spacer(Modifier.height(14.dp))
                    ElevatedCard(shape = RoundedCornerShape(18.dp), colors = CardDefaults.elevatedCardColors(containerColor = Color.White)) {
                        Column(Modifier.fillMaxWidth().padding(16.dp)) {
                            Text("WORKOUT NAME", color = Muted, fontSize = 10.sp, letterSpacing = 1.4.sp, fontWeight = FontWeight.Bold)
                            OutlinedTextField(
                                value = state.workoutName,
                                onValueChange = viewModel::updateName,
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                placeholder = { Text("e.g. Upper body A") },
                            )
                        }
                    }
                    Row(Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Exercises & sets", color = Ink, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                        Spacer(Modifier.weight(1f))
                        Surface(color = Mint, shape = RoundedCornerShape(50)) {
                            Text("${state.sets.size} SETS", color = Green, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp))
                        }
                    }
                    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(Modifier.padding(15.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(color = Mint, shape = RoundedCornerShape(12.dp), modifier = Modifier.size(42.dp)) {
                                    Box(contentAlignment = Alignment.Center) { Icon(Icons.Rounded.FitnessCenter, null, tint = Green) }
                                }
                                Spacer(Modifier.width(11.dp))
                                Column(Modifier.weight(1f)) { Text("Barbell squat", color = Ink, fontWeight = FontWeight.SemiBold); Text("QUADRICEPS · GLUTES", color = Muted, fontSize = 10.sp, letterSpacing = 1.sp) }
                                IconButton(onClick = {}) { Icon(Icons.Rounded.MoreHoriz, "Exercise options", tint = Muted) }
                            }
                            Spacer(Modifier.height(14.dp))
                            Row(Modifier.fillMaxWidth().padding(horizontal = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("SET", Modifier.width(44.dp), color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                TargetHeader("REPS", Modifier.weight(1f))
                                TargetHeader("KG", Modifier.weight(1f))
                                TargetHeader("RPE", Modifier.weight(1f))
                                Spacer(Modifier.width(42.dp))
                            }
                            Spacer(Modifier.height(6.dp))
                        }
                    }
                }
            }
            itemsIndexed(state.sets, key = { _, row -> row.id }) { index, row ->
                SetRow(
                    row = row,
                    onReps = { viewModel.updateSet(row.id, reps = it) },
                    onWeight = { viewModel.updateSet(row.id, weight = it) },
                    onRpe = { viewModel.updateSet(row.id, rpe = it) },
                    onDelete = { viewModel.removeSet(row.id) },
                    onMove = { delta -> viewModel.reorder(index, (index + delta).coerceIn(0, state.sets.lastIndex)) },
                )
            }
            item {
                TextButton(
                    onClick = viewModel::addSet,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                ) { Icon(Icons.Rounded.Add, null, tint = Green); Spacer(Modifier.width(7.dp)); Text("Add set", color = Green, fontWeight = FontWeight.SemiBold) }
                Row(Modifier.fillMaxWidth().padding(top = 3.dp, bottom = 92.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Timer, null, tint = Muted, modifier = Modifier.size(15.dp)); Spacer(Modifier.width(6.dp)); Text("Rest between sets: 90 sec", color = Muted, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun TargetHeader(text: String, modifier: Modifier) = Text(text, modifier, color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = .6.sp)

@Composable
private fun SetRow(row: BuilderSet, onReps: (String) -> Unit, onWeight: (String) -> Unit, onRpe: (String) -> Unit, onDelete: () -> Unit, onMove: (Int) -> Unit) {
    var dragY by remember { mutableFloatStateOf(0f) }
    var dragStep by remember { mutableIntStateOf(0) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(Modifier.fillMaxWidth().padding(start = 7.dp, end = 8.dp, top = 10.dp, bottom = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Rounded.DragIndicator,
                contentDescription = "Drag to reorder",
                tint = Color(0xFF9AA49D),
                modifier = Modifier.width(28.dp).pointerInput(row.id) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = { dragY = 0f; dragStep = 0 },
                        onDragEnd = { dragY = 0f; dragStep = 0 },
                        onDragCancel = { dragY = 0f; dragStep = 0 },
                        onDrag = { change, amount ->
                            change.consume()
                            dragY += amount.y
                            val threshold = size.height.toFloat()
                            if (dragY > threshold) { onMove(1); dragY -= threshold }
                            if (dragY < -threshold) { onMove(-1); dragY += threshold }
                        },
                    )
                },
            )
            Surface(color = Mint, shape = CircleShape, modifier = Modifier.size(30.dp)) {
                Box(contentAlignment = Alignment.Center) { Text("${row.setNumber}", color = Green, fontWeight = FontWeight.Bold, fontSize = 13.sp) }
            }
            Spacer(Modifier.width(8.dp))
            NumberField(row.reps, onReps, Modifier.weight(1f))
            Spacer(Modifier.width(6.dp))
            NumberField(row.weight, onWeight, Modifier.weight(1f))
            Spacer(Modifier.width(6.dp))
            NumberField(row.rpe, onRpe, Modifier.weight(1f))
            IconButton(onClick = onDelete, modifier = Modifier.width(38.dp)) {
                Icon(Icons.Rounded.DeleteOutline, "Remove set", tint = Muted, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
private fun NumberField(value: String, onChange: (String) -> Unit, modifier: Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = { input -> if (input.length <= 6 && input.all { it.isDigit() || it == '.' }) onChange(input) },
        modifier = modifier.height(52.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        shape = RoundedCornerShape(10.dp),
        textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
    )
}
