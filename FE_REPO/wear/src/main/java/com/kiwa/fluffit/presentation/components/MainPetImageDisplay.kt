package com.kiwa.fluffit.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun MainPetImageDisplay(image: Painter) {
//    val image = painterResource(R.drawable.dog_white)
    Image(
        modifier =
        Modifier
            .size(100.dp)
            .pointerInput(Unit) {
                detectTapGestures(

                )
            }
        ,
        painter = image,
        contentDescription = "main page pet image"
    )
}