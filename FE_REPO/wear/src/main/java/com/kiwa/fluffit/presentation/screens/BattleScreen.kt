package com.kiwa.fluffit.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.kiwa.fluffit.model.battle.OpponentInfo
import com.kiwa.fluffit.presentation.battle.BattleViewEvent
import com.kiwa.fluffit.presentation.battle.BattleViewModel
import com.kiwa.fluffit.presentation.battle.BattleViewState
import com.kiwa.fluffit.presentation.battle.ui.DefaultUI
import com.kiwa.fluffit.presentation.battle.ui.LoadingUI

@Composable
fun BattleScreen(
    viewModel: BattleViewModel = hiltViewModel<BattleViewModel>(),
    onStartGame: (OpponentInfo) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val uiState: BattleViewState.Default =
            viewModel.uiState.collectAsState().value as BattleViewState.Default

        DefaultUI(
            Modifier.align(Alignment.Center),
            if (uiState.findMatching) "취소" else "배틀하기",
            uiState.battleLogModel
        ) {
            if (uiState.findMatching)
                viewModel.onTriggerEvent(BattleViewEvent.OnClickCancelBattleButton)
            else
                viewModel.onTriggerEvent(BattleViewEvent.OnClickBattleButton)
        }

        if (uiState.opponentInfo.opponentName.isNotEmpty()) {
            onStartGame(uiState.opponentInfo)
        }

        when (uiState.loading) {
            true ->
                LoadingUI(Modifier.align(Alignment.Center)) {
                    if (uiState.findMatching) Text(
                        text = "상대를 찾는 중입니다.",
                        style = MaterialTheme.typography.caption3
                    )
                }

            false -> {
            }
        }

    }
}

