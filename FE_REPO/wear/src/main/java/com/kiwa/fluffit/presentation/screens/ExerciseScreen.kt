package com.kiwa.fluffit.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wearapp.presentation.HealthViewModel
import com.kiwa.fluffit.presentation.components.CaloriesDisplay
import com.kiwa.fluffit.presentation.components.ExerciseButton
import com.kiwa.fluffit.presentation.components.ExercisePetImageDisplay
import com.kiwa.fluffit.presentation.components.ExerciseTimer
import com.kiwa.fluffit.presentation.components.HeartRateDisplay
import com.kiwa.fluffit.presentation.home.HomeViewModel

@Composable
fun ExerciseScreen() {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val imageUrl by homeViewModel.imageUrl.collectAsState()

    val healthViewModel : HealthViewModel = hiltViewModel()
    val heartRate by healthViewModel.heartRate.collectAsState()
    val calories by healthViewModel.calories.collectAsState()

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(5.dp)){

        HeartRateDisplay(
            modifier = Modifier.align(Alignment.CenterEnd),
            heartRate ?: 0
        )
        CaloriesDisplay(
            modifier = Modifier.align(Alignment.CenterStart),
            calories = calories
        )
        ExerciseTimer(
            modifier =
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = 10.dp)
        )
        ExercisePetImageDisplay(imageUrl.getOrNull(0) ?: "", modifier = Modifier.align(Alignment.Center))
        ExerciseButton()
    }

}
