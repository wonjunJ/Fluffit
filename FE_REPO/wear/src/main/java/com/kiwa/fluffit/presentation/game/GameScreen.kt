package com.kiwa.fluffit.presentation.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.decode.ImageDecoderDecoder
import com.kiwa.fluffit.model.battle.BattleType
import com.kiwa.fluffit.model.battle.GameUIModel
import com.kiwa.fluffit.presentation.game.ui.BattleResultUI
import com.kiwa.fluffit.presentation.game.ui.BreakStoneGameUI
import com.kiwa.fluffit.presentation.game.ui.MatchingCompletedUI
import com.kiwa.fluffit.presentation.game.ui.RaisingHeartBeatUI

@Composable
internal fun GameScreen(
    viewModel: GameViewModel = hiltViewModel<GameViewModel>(),
    gameUIModel: GameUIModel,
    onFinishBattle: () -> Unit,
) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            add(ImageDecoderDecoder.Factory())
        }.build()

    viewModel.onTriggerEvent(GameViewEvent.Init(gameUIModel))

    when (val uiState = viewModel.uiState.collectAsState().value) {
        is GameViewState.Battle -> when (uiState.gameUIModel.battleType) {
            is BattleType.BreakStone -> BreakStoneGameUI(imageLoader) { it ->
                viewModel.onTriggerEvent(
                    GameViewEvent.OnFinishGame(it, uiState.gameUIModel.battleId)
                )
            }

            is BattleType.RaisingHeartBeat -> RaisingHeartBeatUI()
        }

        is GameViewState.BattleResult -> BattleResultUI(uiState, imageLoader) { onFinishBattle() }
        is GameViewState.MatchingCompleted -> MatchingCompletedUI(
            uiState.gameUIModel.opponentInfo
        ) { viewModel.onTriggerEvent(GameViewEvent.OnReadyForGame) }
    }
}
