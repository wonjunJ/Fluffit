package com.kiwa.fluffit.presentation.game

import com.kiwa.fluffit.base.ViewEvent
import com.kiwa.fluffit.model.battle.GameUIModel

sealed class GameViewEvent: ViewEvent {

    data class Init(val gameUIModel: GameUIModel): GameViewEvent()

    data object OnReadyForGame: GameViewEvent()

    data class OnFinishGame(val score: Int, val battleId: String): GameViewEvent()

    data object OnFinishBattle: GameViewEvent()
}
