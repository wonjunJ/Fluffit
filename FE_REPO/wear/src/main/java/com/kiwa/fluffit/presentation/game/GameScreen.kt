package com.kiwa.fluffit.presentation.game

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiwa.fluffit.model.battle.OpponentInfo
import com.kiwa.fluffit.presentation.game.ui.BattleResultUI
import com.kiwa.fluffit.presentation.game.ui.BreakStoneGameUI
import com.kiwa.fluffit.presentation.game.ui.MatchingCompletedUI
import com.kiwa.fluffit.presentation.game.ui.RaisingHeartBeatUI

@Composable
internal fun GameScreen(
    viewModel: GameViewModel = hiltViewModel<GameViewModel>(),
    opponentInfo: OpponentInfo
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is GameViewState.Battle -> when (uiState.battleType) {
            is BattleType.BreakStone -> BreakStoneGameUI { it ->
                Log.d("확인", "게임 끝 호출")
                viewModel.onTriggerEvent(
                    GameViewEvent.OnFinishGame(it, uiState.battleId)
                )
            }

            is BattleType.RaisingHeartBeat -> RaisingHeartBeatUI()
        }

        is GameViewState.BattleResult -> BattleResultUI(uiState)
        is GameViewState.MatchingCompleted -> MatchingCompletedUI(
            opponentInfo
        ) { viewModel.onTriggerEvent(GameViewEvent.OnReadyForGame) }
    }
}
