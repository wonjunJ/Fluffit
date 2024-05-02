package com.kiwa.fluffit.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun CollectionScreen(
    viewModel: CollectionViewModel = hiltViewModel<CollectionViewModel>()
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.collection_background),
            contentDescription = "도감 배경화면",
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .fillMaxWidth(0.7f)
                .align(Alignment.Center)
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxHeight(0.3f).fillMaxWidth(0.5f).background(Color.Blue)
            )
            Box(
                modifier = Modifier.fillMaxHeight(0.3f).fillMaxWidth(0.5f).background(Color.Yellow)
            )
            Box(
                modifier = Modifier.fillMaxHeight(0.3f).fillMaxWidth(0.5f).background(Color.Red)
            )
        }
    }
}
