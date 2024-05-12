package com.kiwa.fluffit.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.kiwa.fluffit.R
import com.kiwa.fluffit.presentation.exercise.ExerciseViewModel
import com.kiwa.fluffit.presentation.theme.fluffitWearFontFamily

@Composable
fun ExerciseButton() {
    val exerciseViewModel : ExerciseViewModel = hiltViewModel()
    val isRunning by exerciseViewModel.isTimerRunning.collectAsState()

    val buttonText = if (isRunning)
         stringResource(R.string.exercise_stop_button) else  stringResource(R.string.exercise_button)

    Box(modifier = Modifier
        .padding(5.dp)
        .fillMaxSize()) {
        Button(
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .width(80.dp)
                .height(30.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 4.dp),
            onClick = {
                exerciseViewModel.startTimer()
            }
        ) {
            Text(text = buttonText, fontFamily = fluffitWearFontFamily)
        }
    }
}
