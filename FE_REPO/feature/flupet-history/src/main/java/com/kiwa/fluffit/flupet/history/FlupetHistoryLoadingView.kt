package com.kiwa.fluffit.flupet.history

import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kiwa.fluffit.flupet_history.R

@Composable
internal fun FlupetHistoryLoadingView() {
    val context = LocalContext.current
    val glide = remember { Glide.with(context) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        AndroidView(
            factory = { ctx ->
                ImageView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            update = { imageView ->
                glide
                    .asGif()
                    .load(R.raw.hourglass)
                    .apply(RequestOptions().fitCenter())
                    .into(imageView)
                    .clearOnDetach()
            }
        )
    }
}
