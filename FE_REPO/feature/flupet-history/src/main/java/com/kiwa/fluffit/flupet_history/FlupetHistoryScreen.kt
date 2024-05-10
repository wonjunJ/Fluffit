package com.kiwa.fluffit.flupet_history

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun FlupetHistoryScreen(){
    Box(){
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.flupet_history_background),
            contentDescription = "배경화면",
            contentScale = ContentScale.FillBounds
        )
    }
}
