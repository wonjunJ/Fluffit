package com.kiwa.fluffit.flupet.history

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kiwa.fluffit.designsystem.theme.fluffitTypography
import com.kiwa.fluffit.model.flupet.response.MyFlupets

@SuppressLint("UnrememberedMutableState")
@Composable
fun FlupetLog(
    myFlupet: MyFlupets
) {
    val context = LocalContext.current
    val glide = remember { Glide.with(context) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .aspectRatio(1f)
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
                        val imageUrl = myFlupet.imageUrl[0]
                        glide
                            .asGif()
                            .load(imageUrl)
                            .apply(RequestOptions().fitCenter())
                            .into(imageView)
                            .clearOnDetach()
                    }
                )
            }
            Spacer(modifier = Modifier.width(40.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.2f)
                    .fillMaxWidth(0.9f)
            ) {
                Text(
                    text = myFlupet.name,
                    style = fluffitTypography.headlineLarge.merge(
                        textAlign = TextAlign.Start
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = myFlupet.birthDay + " ~\n" + myFlupet.endDay,
                    style = fluffitTypography.headlineSmall.merge(
                        textAlign = TextAlign.Start
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "육성기간\n" + myFlupet.age,
                    style = fluffitTypography.headlineSmall.merge(
                        textAlign = TextAlign.Start
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = myFlupet.steps.toString() + "걸음",
                    style = fluffitTypography.headlineSmall.merge(
                        textAlign = TextAlign.Start
                    )
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}
