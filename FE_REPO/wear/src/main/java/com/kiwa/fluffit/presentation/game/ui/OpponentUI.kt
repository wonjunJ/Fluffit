package com.kiwa.fluffit.presentation.game.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import com.kiwa.fluffit.model.battle.OpponentInfo

@Composable
internal fun OpponentUI(opponentInfo: OpponentInfo, modifier: Modifier) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(ImageDecoderDecoder.Factory())
        }.build()

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "VS", style = MaterialTheme.typography.display2)
        Text(text = opponentInfo.opponentName, style = MaterialTheme.typography.display1)
        Image(
            painter = rememberAsyncImagePainter(
                imageLoader = imageLoader,
                model = opponentInfo.opponentFlupetImage,
            ),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp).scale(scaleX = -1f, scaleY = 1f)
        )
        Text(text = "${opponentInfo.opponentBattlePoint}점", style = MaterialTheme.typography.display1)
    }
}
