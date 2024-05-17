package com.kiwa.fluffit.battle.record

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kiwa.fluffit.battle_record.R


@Composable
fun CrownItemView(
    isWin: Boolean,
    modifier: Modifier = Modifier
) {
    if (isWin) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(2f)){
                Image(
                    painter = painterResource(id = R.drawable.winners_crown),
                    modifier = Modifier.fillMaxWidth(0.3f).aspectRatio(1f).align(Alignment.Center),
                    contentDescription = "승리 왕관",
                    contentScale = ContentScale.FillBounds
                )
            }
            Box(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.weight(2f))
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(2f))
            Box(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.weight(2f)){
                Image(
                    painter = painterResource(id = R.drawable.winners_crown),
                    modifier = Modifier.fillMaxWidth(0.3f).aspectRatio(1f).align(Alignment.Center),
                    contentDescription = "승리 왕관",
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}
