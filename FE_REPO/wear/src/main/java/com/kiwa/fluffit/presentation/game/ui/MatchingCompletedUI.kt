package com.kiwa.fluffit.presentation.game.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kiwa.fluffit.model.battle.OpponentInfo
import kotlinx.coroutines.delay

@Composable
internal fun MatchingCompletedUI(opponentInfo: OpponentInfo, onReadyForBattle: () -> Unit) {

    val timer = remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (timer.intValue <= 10) {
            delay(1000)
            timer.intValue += 1
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (timer.intValue) {
            in 0..5 -> {
                OpponentUI(opponentInfo, Modifier.align(Alignment.Center))
            }

            else -> {
                onReadyForBattle()
            }
        }
    }

}
