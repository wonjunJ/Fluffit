package com.kiwa.fluffit.presentation.components

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun PetImageDisplay(image: Painter) {
//    val image = painterResource(R.drawable.dog_white)
    Image(
        modifier = Modifier.size(100.dp),
        painter = image,
        contentDescription = "Cute Dog"
    )
}