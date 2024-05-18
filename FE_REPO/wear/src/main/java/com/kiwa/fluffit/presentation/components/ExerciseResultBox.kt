package com.kiwa.fluffit.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.example.wearapp.presentation.HealthViewModel
import com.kiwa.fluffit.presentation.exercise.ExerciseViewModel
import kotlinx.coroutines.launch

@Composable
fun ExerciseResultBox(healthViewModel: HealthViewModel, exerciseViewModel: ExerciseViewModel) {
    val distance = healthViewModel.getDistance()
    val calories = healthViewModel.getCalories()

    val visible by healthViewModel.endRunning.collectAsState()

    AnimatedVisibility(
        modifier =
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if (visible!!) {
                            healthViewModel.turnOffResult()
                            healthViewModel.stopRunning()
                            exerciseViewModel.resetTimer()
                        }
                    }
                )
            },
        visible = visible!!,
        enter = fadeIn(animationSpec = tween(durationMillis = 300)), // 점점 밝아지며 나타나는 효과
        exit = fadeOut(animationSpec = tween(durationMillis = 300))  // 점점 어두워지며 사라지는 효과
    ) {
        Box{
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(color = Color.White, shape = RoundedCornerShape(30.dp))
                    .width(120.dp)
                    .height(120.dp)
                    .padding(5.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) {
                    Text(
                        text = "소모 칼로리",
                        color = Color.Black,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "${calories?.toInt()} kcal",
                        color = Color.Black,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 13.dp)
                    )
                    Text(
                        text = "이동 거리",
                        color = Color.Black,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "${distance?.toInt()}m",
                        color = Color.Black,
                        fontSize = 13.sp,
                    )
                }
            }
        }

    }

}
