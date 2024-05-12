package com.kiwa.fluffit.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiwa.fluffit.presentation.components.ExerciseButton
import com.kiwa.fluffit.presentation.components.ExerciseTimer
import com.kiwa.fluffit.presentation.components.HeartRateDisplay

@Composable
fun ExerciseScreen() {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(5.dp)){

        HeartRateDisplay(modifier = Modifier.align(Alignment.CenterEnd))
        ExerciseTimer(
            modifier =
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = 5.dp)
        )
        ExerciseButton()
    }

}
