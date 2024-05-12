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
import com.kiwa.fluffit.presentation.battle.BattleType
import com.kiwa.fluffit.presentation.battle.BattleViewEvent
import com.kiwa.fluffit.presentation.battle.BattleViewModel
import com.kiwa.fluffit.presentation.battle.BattleViewState
import com.kiwa.fluffit.presentation.battle.ui.MatchingCompletedUI
import com.kiwa.fluffit.presentation.battle.ui.BreakStoneGameUI
import com.kiwa.fluffit.presentation.battle.ui.DefaultUI
import com.kiwa.fluffit.presentation.battle.ui.LoadingUI
import com.kiwa.fluffit.presentation.battle.ui.RaisingHeartBeatUI

@Composable
fun BattleScreen(viewModel: BattleViewModel = hiltViewModel<BattleViewModel>()) {
    Box(modifier = Modifier.fillMaxSize()) {
        val uiState = viewModel.uiState.collectAsState().value
        when (uiState) {
            is BattleViewState.Default -> DefaultUI(
                Modifier.align(Alignment.Center),
                if (uiState.findMatching) "취소" else "배틀하기",
                uiState.battleLogModel
            ) {
                if (uiState.findMatching)
                    viewModel.onTriggerEvent(BattleViewEvent.OnClickCancelBattleButton)
                else
                    viewModel.onTriggerEvent(BattleViewEvent.OnClickBattleButton)
            }

            is BattleViewState.MatchingCompleted -> MatchingCompletedUI(
                uiState
            ) { viewModel.onTriggerEvent(BattleViewEvent.OnReadyForBattle) }

            is BattleViewState.Battle -> when (uiState.battleType) {
                is BattleType.BreakStone -> BreakStoneGameUI()
                is BattleType.RaisingHeartBeat -> RaisingHeartBeatUI()
            }

            is BattleViewState.BattleResult -> TODO()
        }

        when (uiState.loading) {
            true ->
                LoadingUI(Modifier.align(Alignment.Center)) {
                    if (uiState is BattleViewState.Default && uiState.findMatching) Text(
                        text = "상대를 찾는 중입니다.",
                        style = MaterialTheme.typography.caption3
                    )
                }

            false -> {
            }
        }

    }
}

