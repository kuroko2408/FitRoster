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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

private val FoodBg = Color(0xFFF4F7F4)
private val FoodInk = Color(0xFF17221D)
private val FoodMuted = Color(0xFF77827B)
private val FoodGreen = Color(0xFF2E7D57)
private val FoodMint = Color(0xFFE7F2EA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodLoggingScreen(viewModel: AthleteNutritionViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val summary = state.summary
    Scaffold(
        containerColor = FoodBg,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Food & macros", color = FoodInk, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                        Text("ATHLETE NUTRITION", color = FoodMuted, fontSize = 10.sp, letterSpacing = 1.5.sp)
                    }
                },
                navigationIcon = { IconButton(onClick = {}) { Icon(Icons.Rounded.ArrowBack, "Back", tint = FoodInk) } },
                actions = { IconButton(onClick = viewModel::refreshSummary) { Icon(Icons.Rounded.Refresh, "Refresh summary", tint = FoodGreen) } },
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
                    Text("Your daily fuel", color = FoodInk, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Text(summary?.date ?: "Today", color = FoodMuted, fontSize = 13.sp, modifier = Modifier.padding(top = 5.dp))
                }
            }
            item {
                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.fillMaxWidth().padding(17.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("Today's summary", color = FoodInk, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("Logged so far vs daily target", color = FoodMuted, fontSize = 11.sp)
                            }
                            if (state.isLoading) CircularProgressIndicator(Modifier.size(20.dp), color = FoodGreen, strokeWidth = 2.dp)
                        }
                        Spacer(Modifier.height(16.dp))
                        MacroSummaryRow("Protein", summary?.consumed?.protein ?: 0.0, summary?.targets?.proteinTarget, "g", FoodGreen)
                        MacroSummaryRow("Carbohydrates", summary?.consumed?.carbs ?: 0.0, summary?.targets?.carbTarget, "g", Color(0xFF477D9B))
                        MacroSummaryRow("Fats", summary?.consumed?.fats ?: 0.0, summary?.targets?.fatTarget, "g", Color(0xFFC58338))
                        MacroSummaryRow("Calories", summary?.consumed?.calories ?: 0.0, summary?.targets?.calorieTarget, "kcal", Color(0xFF8D6AB1))
                        if (!state.isLoading && summary?.targets == null) {
                            Text("Your coach hasn't assigned daily targets yet.", color = FoodMuted, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                }
                if (state.message != null && state.isError) {
                    Text(state.message!!, color = Color(0xFFB3261E), fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                }
            }
            item {
                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.fillMaxWidth().padding(17.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(color = FoodMint, shape = RoundedCornerShape(11.dp)) {
                                Icon(Icons.Rounded.Restaurant, null, tint = FoodGreen, modifier = Modifier.padding(8.dp))
                            }
                            Column(Modifier.padding(start = 10.dp)) {
                                Text("Log a meal", color = FoodInk, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("Add the nutrition for what you ate", color = FoodMuted, fontSize = 11.sp)
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        OutlinedTextField(
                            value = state.foodName,
                            onValueChange = viewModel::updateFoodName,
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Food or meal name") },
                            placeholder = { Text("e.g. Chicken rice bowl") },
                            singleLine = true,
                            shape = RoundedCornerShape(13.dp),
                        )
                        Spacer(Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            MacroInputField("Protein", state.protein, "g", viewModel::updateProtein, Modifier.weight(1f))
                            MacroInputField("Carbs", state.carbohydrates, "g", viewModel::updateCarbohydrates, Modifier.weight(1f))
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            MacroInputField("Fats", state.fats, "g", viewModel::updateFats, Modifier.weight(1f))
                            MacroInputField("Calories", state.calories, "kcal", viewModel::updateCalories, Modifier.weight(1f))
                        }
                        Button(
                            onClick = viewModel::logMeal,
                            enabled = !state.isSaving,
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = FoodGreen),
                        ) {
                            if (state.isSaving) CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                            else {
                                Icon(Icons.Rounded.Add, null)
                                Spacer(Modifier.width(7.dp))
                                Text("Add meal to today", fontWeight = FontWeight.SemiBold)
                            }
                        }
                        if (state.message != null && !state.isError) {
                            Text(state.message!!, color = FoodGreen, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MacroSummaryRow(label: String, consumed: Double, target: Double?, unit: String, tint: Color) {
    val ratio = if (target == null || target <= 0.0) 0f else (consumed / target).toFloat().coerceIn(0f, 1f)
    Column(Modifier.fillMaxWidth().padding(bottom = 13.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(label, color = FoodInk, fontSize = 12.sp, modifier = Modifier.weight(1f))
            Text("${consumed.pretty()} $unit", color = FoodInk, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            Text(" / ${target?.pretty() ?: "—"} $unit", color = FoodMuted, fontSize = 11.sp)
        }
        LinearProgressIndicator(
            progress = { ratio },
            modifier = Modifier.fillMaxWidth().padding(top = 7.dp).height(6.dp),
            color = tint,
            trackColor = Color(0xFFE9EEEA),
        )
    }
}

private fun Double.pretty(): String = if (this % 1.0 == 0.0) toInt().toString() else "%.1f".format(this)
