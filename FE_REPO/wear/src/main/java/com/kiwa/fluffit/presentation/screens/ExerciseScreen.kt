package com.kiwa.fluffit.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wearapp.presentation.HealthViewModel
import com.kiwa.fluffit.presentation.components.DistanceDisplay
import com.kiwa.fluffit.presentation.components.ExerciseButton
import com.kiwa.fluffit.presentation.components.ExercisePetImageDisplay
import com.kiwa.fluffit.presentation.components.ExerciseResultBox
import com.kiwa.fluffit.presentation.components.ExerciseTimer
import com.kiwa.fluffit.presentation.components.HeartRateDisplay
import com.kiwa.fluffit.presentation.exercise.ExerciseViewModel
import com.kiwa.fluffit.presentation.home.HomeViewModel

@Composable
fun ExerciseScreen() {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val imageUrl by homeViewModel.imageUrl.collectAsState()
    val exerciseViewModel : ExerciseViewModel = hiltViewModel()
    val timer by exerciseViewModel.timerValue.collectAsState()

    val healthViewModel : HealthViewModel = hiltViewModel()
    val heartRate by healthViewModel.heartRate.collectAsState()
    val distance by healthViewModel.runningDistance.collectAsState()
    val calories by healthViewModel.runningCalories.collectAsState()

    val isRunning by exerciseViewModel.isTimerRunning.collectAsState()
    var image = ""

    if (imageUrl.isNotEmpty()) {
        image = if (imageUrl.size >= 2) {
            imageUrl[1]
        } else {
            imageUrl[0]
        }
    }

    if(isRunning) {
        if (imageUrl.size >= 5) {
            image = imageUrl[4]
        }
//
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(5.dp)){

        HeartRateDisplay(
            modifier = Modifier.align(Alignment.CenterEnd),
            heartRate ?: 0
        )
        DistanceDisplay(
            modifier = Modifier.align(Alignment.CenterStart),
            distance = distance ?: 0.0,
            calories = calories ?: 0.0
        )
        ExerciseTimer(
            modifier =
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = 10.dp),
            timer = timer
        )
        ExercisePetImageDisplay(image!!, modifier = Modifier.align(Alignment.Center))
        ExerciseButton()
        ExerciseResultBox(healthViewModel, exerciseViewModel)
    }

}
