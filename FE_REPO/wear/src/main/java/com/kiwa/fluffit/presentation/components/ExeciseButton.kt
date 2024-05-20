package com.kiwa.fluffit.presentation.components

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.kiwa.fluffit.presentation.health.HealthViewModel
import com.kiwa.fluffit.R
import com.kiwa.fluffit.presentation.exercise.ExerciseViewModel
import com.kiwa.fluffit.presentation.theme.fluffitWearFontFamily
import com.kiwa.fluffit.presentation.util.sendMessageToPhone
import kotlinx.coroutines.launch

@Composable
fun ExerciseButton() {
    val exerciseViewModel: ExerciseViewModel = hiltViewModel()
    val healthViewModel: HealthViewModel = hiltViewModel()
    val isRunning by exerciseViewModel.isTimerRunning.collectAsState()
    val calories by healthViewModel.runningCalories.collectAsState()

    val startTime by exerciseViewModel.startTime.collectAsState()
    val endTime by exerciseViewModel.endTime.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    val buttonText = if (isRunning)
        stringResource(R.string.exercise_stop_button) else stringResource(R.string.exercise_button)

    val context = LocalContext.current

    val onCoinUpdate: () -> Unit = { sendMessageToPhone(context) }

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
                if (isRunning) {
                    exerciseViewModel.pauseTimer()
                    healthViewModel.pauseRunning()
                    coroutineScope.launch {
                        val coin = healthViewModel.sendRunningRequest(
                            calories = calories!!.toInt(),
                            startTime = startTime!!,
                            endTime = endTime!!
                        )
                        Toast.makeText(context, "+${coin}코인", Toast.LENGTH_SHORT).show()
                    }
                    onCoinUpdate()
//                    Toast.makeText(context, "Exercise Paused", Toast.LENGTH_SHORT).show()
                } else {
                    healthViewModel.startRunning()
                    exerciseViewModel.startTimer()
//                    Toast.makeText(context, "Exercise Started", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text(text = buttonText, fontFamily = fluffitWearFontFamily)
        }
    }
}
