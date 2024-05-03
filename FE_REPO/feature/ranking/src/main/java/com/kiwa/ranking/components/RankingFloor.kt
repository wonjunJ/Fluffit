package com.kiwa.ranking.components

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.size.OriginalSize
import com.kiwa.fluffit.ranking.R

@Composable
fun RankingFloor(rank: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.build()
    val floorImage = when (rank) {
        1 -> R.drawable.first_place
        2 -> R.drawable.second_place
        else -> R.drawable.third_place
    }
    val floorSize = when (rank) {
        1 -> 90
        2 -> 70
        else -> 60
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.then(Modifier.padding(vertical = 12.dp))
    ) {
        val textStyle =
            MaterialTheme.typography.headlineSmall.merge(fontSize = 12.sp, color = Color.White)
        OutLinedText(
            onClickText = {},
            text = "유저이름여덟임다",
            textStyle = textStyle,
            strokeColor = Color.Black,
            strokeWidth = 1f
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutLinedText(
            onClickText = {},
            text = "펫이름입니다",
            textStyle = textStyle,
            strokeColor = Color.Black,
            strokeWidth = 1f
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutLinedText(
            onClickText = {},
            text = "점수또는시간",
            textStyle = textStyle,
            strokeColor = Color.Black,
            strokeWidth = 1f
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(modifier = Modifier.height((floorSize + 8).dp)) {
            Image(
                painter = painterResource(id = floorImage),
                contentDescription = null,
                modifier = Modifier
                    .size(floorSize.dp)
                    .align(Alignment.BottomCenter)
            )
            Image(
                painter = rememberImagePainter(
                    imageLoader = imageLoader,
                    data = "https://github.com/shjung53/algorithm_study/assets/" +
                        "90888718/4399f85d-7810-464c-ad76-caae980ce047",
                    builder = {
                        size(OriginalSize)
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.TopCenter)
            )
        }
    }
}
