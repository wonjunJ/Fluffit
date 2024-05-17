package com.kiwa.fluffit.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.kiwa.fluffit.home.HomeViewState
import com.kiwa.fluffit.home.R
import kotlinx.coroutines.delay

@Composable
internal fun EvolutionDialog(
    uiState: HomeViewState.Default,
    modifier: Modifier,
    imageLoader: ImageLoader,
    onEndEvolution: () -> Unit
) {
    val brightness = remember { mutableFloatStateOf(1f) }
    val beforeImage = uiState.beforeImage
    val afterImage = uiState.flupet.imageUrls.nodding
    val durationMillis = 3000
    val frameDelay = 16L

    val fadeIn = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        for (i in 0..durationMillis / frameDelay.toInt()) {
            brightness.floatValue = 1f - i / (durationMillis / frameDelay.toFloat())
            delay(frameDelay)
        }
        fadeIn.value = true
    }

    LaunchedEffect(fadeIn.value) {
        if (fadeIn.value) {
            for (i in 0..durationMillis / frameDelay.toInt()) {
                brightness.floatValue = i / (durationMillis / frameDelay.toFloat())
                delay(frameDelay)
            }
            onEndEvolution()
        }
    }

    Box(
        modifier = modifier
            .size(300.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White)
            .graphicsLayer {
                alpha = brightness.floatValue
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.main_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (fadeIn.value) {
            Image(
                painter = rememberAsyncImagePainter(
                    imageLoader = imageLoader,
                    model = afterImage
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.Center)
            )
        } else {
            Image(
                painter = rememberAsyncImagePainter(
                    imageLoader = imageLoader,
                    model = beforeImage
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.Center)
            )
        }
    }
}
