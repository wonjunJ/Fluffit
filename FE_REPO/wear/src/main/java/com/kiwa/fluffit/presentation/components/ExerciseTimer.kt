package com.kiwa.fluffit.presentation.components

//import ExerciseViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Text
import com.kiwa.fluffit.presentation.exercise.ExerciseViewModel
import com.kiwa.fluffit.presentation.theme.fluffitWearFontFamily

@Composable
fun ExerciseTimer(modifier: Modifier, timer : String) {
    Text(
        modifier = modifier,
        textAlign = TextAlign.Center,
        text = timer,
        color = Color.White,
        fontSize = 20.sp
    )

}
