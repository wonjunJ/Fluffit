package com.kiwa.fluffit.presentation.game.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.kiwa.fluffit.R
import com.kiwa.fluffit.presentation.game.GameViewState

@Composable
internal fun BattleResultUI(uiState: GameViewState.BattleResult) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "배틀 결과", style = MaterialTheme.typography.display1, modifier = Modifier
                .align(
                    Alignment.TopCenter
                )
                .padding(top = 16.dp)
        )
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 36.dp)
                ) {
                    if (uiState.result.isWin) {
                        Image(
                            painter = painterResource(id = R.drawable.crown),
                            modifier = if (uiState.result.isWin) Modifier
                                .size(32.dp)
                                .align(Alignment.CenterStart) else Modifier
                                .size(32.dp)
                                .align(
                                    Alignment.TopCenter
                                ),
                            contentDescription = null
                        )
                    }
                    Text(
                        text = uiState.result.myBattleScore,
                        style = MaterialTheme.typography.display1.merge(
                            colorResource(id = R.color.watchBlue)
                        ),
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
                Text(text = "vs", style = MaterialTheme.typography.display1)
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 36.dp)
                ) {
                    if (!uiState.result.isWin) {
                        Image(
                            painter = painterResource(id = R.drawable.crown),
                            modifier = if (uiState.result.isWin) Modifier
                                .size(32.dp)
                                .align(Alignment.CenterStart) else Modifier
                                .size(32.dp)
                                .align(
                                    Alignment.TopCenter
                                ),
                            contentDescription = null
                        )
                    }
                    Text(
                        text = uiState.result.opponentBattleScore,
                        style = MaterialTheme.typography.display1.merge(
                            colorResource(id = R.color.watchRed)
                        ),
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }
        Text(
            text = "배틀포인트: ${uiState.result.battlePoint}",
            style = MaterialTheme.typography.display2,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        )
    }

}
