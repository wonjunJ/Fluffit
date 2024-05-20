package com.kiwa.fluffit.presentation.game

import com.kiwa.fluffit.base.ViewState
import com.kiwa.fluffit.model.battle.BattleResultUIModel
import com.kiwa.fluffit.model.battle.BattleType
import com.kiwa.fluffit.model.battle.GameUIModel
import com.kiwa.fluffit.model.battle.OpponentInfo

sealed class GameViewState : ViewState {

    abstract val loading: Boolean
    abstract val gameUIModel: GameUIModel

    data class MatchingCompleted(
        override val loading: Boolean = false,
        override val gameUIModel: GameUIModel = GameUIModel(
            battleId = "", opponentInfo = OpponentInfo(
                "", "", "", 0
            ),
            key = "",
            title = "",
            description = "",
            time = 10
        ),
    ) : GameViewState()

    data class Battle(
        val score: Int,
        override val loading: Boolean, override val gameUIModel: GameUIModel,
    ) : GameViewState()

    data class BattleResult(
        val result: BattleResultUIModel,
        override val loading: Boolean,
        override val gameUIModel: GameUIModel,
    ) : GameViewState()
}
