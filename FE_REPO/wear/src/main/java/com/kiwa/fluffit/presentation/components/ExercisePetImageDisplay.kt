package com.kiwa.fluffit.presentation.components

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest

@Composable
fun ExercisePetImageDisplay(imageUrl: String, modifier: Modifier) {
    val context = LocalContext.current

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(context)
            .data(imageUrl)
            .apply {
                // Use ImageDecoderDecoder if API level is 28 or higher, else use GifDecoder
                decoderFactory(if (Build.VERSION.SDK_INT >= 28) ImageDecoderDecoder.Factory() else GifDecoder.Factory())
            }
            .build())

    Image(
        modifier = modifier.size(
            if(
                imageUrl.contains("/white_rabbit_run.gif") ||
                imageUrl.contains("/nero_run.gif") ||
                imageUrl.contains("/corgi_run.gif")
            ) {
                190.dp
            } else {
                100.dp
            }
        ),
        painter = painter,
        contentDescription = "exercise page pet image"
    )
}
