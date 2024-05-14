package com.kiwa.ranking.components

import android.os.Build
import androidx.annotation.DrawableRes
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
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.kiwa.fluffit.designsystem.theme.bronze
import com.kiwa.fluffit.designsystem.theme.gold
import com.kiwa.fluffit.designsystem.theme.silver
import com.kiwa.fluffit.model.ranking.RankingInfo
import com.kiwa.fluffit.ranking.R

@Composable
fun RankingFloor(rankingInfo: RankingInfo, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.build()

    @DrawableRes val floorImage: Int
    val floorSize: Int
    val textColor: Color

    when (rankingInfo.rank) {
        1 -> {
            floorImage = R.drawable.first_place
            floorSize = 90
            textColor = gold
        }

        2 -> {
            floorImage = R.drawable.second_place
            floorSize = 70
            textColor = silver
        }

        else -> {
            floorImage = R.drawable.third_place
            floorSize = 60
            textColor = bronze
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.then(Modifier.padding(vertical = 12.dp))
    ) {
        val textStyle =
            MaterialTheme.typography.headlineSmall.merge(fontSize = 12.sp, color = textColor)
        OutLinedText(
            onClickText = {},
            text = rankingInfo.userNickname,
            textStyle = textStyle,
            strokeColor = Color.Black,
            strokeWidth = 1f
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutLinedText(
            onClickText = {},
            text = rankingInfo.petName,
            textStyle = textStyle,
            strokeColor = Color.Black,
            strokeWidth = 1f
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutLinedText(
            onClickText = {},
            text = rankingInfo.score,
            textStyle = textStyle,
            strokeColor = Color.Black,
            strokeWidth = 1f
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.height((floorSize + 8).dp)) {
            Image(
                painter = painterResource(id = floorImage),
                contentDescription = null,
                modifier = Modifier
                    .size(floorSize.dp)
                    .align(Alignment.BottomCenter)
            )
            Image(
                painter = rememberAsyncImagePainter(
                    imageLoader = imageLoader,
                    model = rankingInfo.petImageUrl
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.TopCenter)
            )
        }
    }
}
