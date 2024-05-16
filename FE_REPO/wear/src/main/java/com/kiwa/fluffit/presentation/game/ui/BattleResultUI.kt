package com.kiwa.fluffit.presentation.game.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.kiwa.fluffit.R
import com.kiwa.fluffit.presentation.game.GameViewState
import kotlinx.coroutines.delay

@Composable
internal fun BattleResultUI(
    uiState: GameViewState.BattleResult,
    imageLoader: ImageLoader,
    onFinishBattle: () -> Unit,
) {
    val pointBefore =
        if (uiState.result.isWin) uiState.result.battlePoint - uiState.result.pointDiff
        else uiState.result.battlePoint + uiState.result.pointDiff

    val pointAfter = uiState.result.battlePoint
    val point = remember { mutableIntStateOf(pointBefore) }

    LaunchedEffect(Unit) {
        while (point.intValue < pointAfter) {
            point.intValue++
            delay(30)
        }
    }

    LaunchedEffect(Unit) {
        delay(3000)
        onFinishBattle()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "배틀 결과", style = MaterialTheme.typography.display1, modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 24.dp)
                .align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .fillMaxHeight()
                    .align(Alignment.CenterStart)
            ) {
                if (uiState.result.isWin) {
                    Image(
                        painter = painterResource(id = R.drawable.crown),
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.TopCenter),
                        contentDescription = null,
                    )
                }
                Text(
                    text = uiState.result.myBattleScore.toString(),
                    style = MaterialTheme.typography.display1.merge(
                        colorResource(id = R.color.watchBlue)
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Text(
                text = "vs",
                style = MaterialTheme.typography.display1,
                modifier = Modifier.align(Alignment.Center)
            )
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd)
            ) {
                if (!uiState.result.isWin) {
                    Image(
                        painter = painterResource(id = R.drawable.crown),
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.TopCenter),
                        contentDescription = null,
                    )
                }
                Text(
                    text = uiState.result.opponentBattleScore.toString(),
                    style = MaterialTheme.typography.display1.merge(
                        colorResource(id = R.color.watchRed)
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .wrapContentWidth()
        ) {
            Text(
                text = "${point.intValue}p",
                style = MaterialTheme.typography.display1,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 40.dp)
            )

            Image(
                painter = rememberAsyncImagePainter(
                    imageLoader = imageLoader,
                    model = if (uiState.result.isWin) R.drawable.increase else R.drawable.decrease,
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterStart)
            )
        }
    }

}
