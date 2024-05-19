package com.kiwa.fluffit.presentation.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.*
import com.example.wearapp.presentation.HealthViewModel
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

                    Log.d("TAG", "운동 요청 ")
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
