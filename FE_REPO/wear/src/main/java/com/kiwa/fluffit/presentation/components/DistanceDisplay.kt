package com.kiwa.fluffit.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import kotlinx.coroutines.delay

@Composable
fun DistanceDisplay(modifier: Modifier, distance: Double, calories: Double) {
    val distanceInMeters = distance.toInt()
    val caloriesToInt = calories.toInt()
    var showDistance by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000) // 2초마다 상태 변경
            showDistance = !showDistance
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            text = if (showDistance) "$distanceInMeters" else "$caloriesToInt",
            color = Color.White
        )
        Text(
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            text = if (showDistance) "M" else "kcal",
            color = Color.White
        )
    }
}
