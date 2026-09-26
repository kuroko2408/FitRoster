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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

private val NutritionBg = Color(0xFFF4F7F4)
private val NutritionInk = Color(0xFF17221D)
private val NutritionMuted = Color(0xFF77827B)
private val NutritionGreen = Color(0xFF2E7D57)
private val NutritionMint = Color(0xFFE7F2EA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MacroAllocatorScreen(viewModel: CoachNutritionViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        containerColor = NutritionBg,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Macro allocator", color = NutritionInk, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                        Text("ATHLETE NUTRITION", color = NutritionMuted, fontSize = 10.sp, letterSpacing = 1.5.sp)
                    }
                },
                navigationIcon = { IconButton(onClick = {}) { Icon(Icons.Rounded.ArrowBack, "Back", tint = NutritionInk) } },
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
                    Text("Set daily targets", color = NutritionInk, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Text("Give your athlete a clear nutrition goal for each day.", color = NutritionMuted, fontSize = 13.sp, modifier = Modifier.padding(top = 5.dp))
                }
            }
            item {
                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.fillMaxWidth().padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(color = NutritionMint, shape = RoundedCornerShape(12.dp)) {
                                Icon(Icons.Rounded.Restaurant, null, tint = NutritionGreen, modifier = Modifier.padding(9.dp))
                            }
                            Column(Modifier.padding(start = 11.dp)) {
                                Text("Daily nutrition", color = NutritionInk, fontWeight = FontWeight.Bold)
                                Text("Targets are shown in grams and kcal", color = NutritionMuted, fontSize = 11.sp)
                            }
                        }
                        Spacer(Modifier.height(19.dp))
                        MacroInputField("Protein", state.protein, "g", viewModel::updateProtein)
                        MacroInputField("Carbohydrates", state.carbohydrates, "g", viewModel::updateCarbohydrates)
                        MacroInputField("Fats", state.fats, "g", viewModel::updateFats)
                        MacroInputField("Calories", state.calories, "kcal", viewModel::updateCalories)
                    }
                }
            }
            item {
                Button(
                    onClick = viewModel::saveTargets,
                    enabled = !state.isSaving,
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NutritionGreen),
                ) {
                    if (state.isSaving) CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                    else {
                        Icon(Icons.Rounded.Bolt, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Save daily targets", fontWeight = FontWeight.SemiBold)
                    }
                }
                state.message?.let { message ->
                    Text(message, color = if (state.isError) Color(0xFFB3261E) else NutritionGreen, fontSize = 13.sp, modifier = Modifier.padding(top = 10.dp))
                }
            }
        }
    }
}

@Composable
internal fun MacroInputField(
    label: String,
    value: String,
    unit: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { input -> if (input.length <= 7 && input.all { it.isDigit() || it == '.' }) onValueChange(input) },
        modifier = modifier.fillMaxWidth().padding(bottom = 10.dp),
        label = { Text(label) },
        trailingIcon = { Text(unit, color = NutritionMuted, modifier = Modifier.padding(end = 12.dp)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        shape = RoundedCornerShape(13.dp),
    )
}
