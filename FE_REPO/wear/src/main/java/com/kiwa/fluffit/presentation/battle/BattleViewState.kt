package com.kiwa.fluffit.presentation.battle

import com.kiwa.fluffit.base.ViewState
import com.kiwa.fluffit.model.battle.BattleStatisticsUIModel
import com.kiwa.fluffit.model.battle.GameUIModel
import com.kiwa.fluffit.model.battle.OpponentInfo

sealed class BattleViewState : ViewState {

    abstract val loading: Boolean
    abstract val message: String

    data class Default(
        val battleStatistics: BattleStatisticsUIModel = BattleStatisticsUIModel(0, 0, 0, 0.0, 0),
        val gameUIModel: GameUIModel = GameUIModel(
            battleId = "", opponentInfo = OpponentInfo(
                "", "", "", 0
            ),
            key = "",
            title = "",
            description = "",
            0
        ),
        override val loading: Boolean = false,
        val findMatching: Boolean = false,
        override val message: String = "",
    ) : BattleViewState()
}
