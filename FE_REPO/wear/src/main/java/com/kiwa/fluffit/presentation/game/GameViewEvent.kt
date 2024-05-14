package com.kiwa.fluffit.presentation.game

import com.kiwa.fluffit.base.ViewEvent

sealed class GameViewEvent: ViewEvent {

    data object OnReadyForGame: GameViewEvent()

    data class OnFinishGame(val score: Int, val battleId: String): GameViewEvent()

    data object OnFinishBattle: GameViewEvent()
}
