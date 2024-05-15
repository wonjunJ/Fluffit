package com.kiwa.fluffit.presentation.battle

import com.kiwa.fluffit.base.ViewState
import com.kiwa.fluffit.model.battle.BattleLogModel
import com.kiwa.fluffit.model.battle.BattleType
import com.kiwa.fluffit.model.battle.GameUIModel
import com.kiwa.fluffit.model.battle.OpponentInfo

sealed class BattleViewState : ViewState {

    abstract val loading: Boolean
    abstract val message: String

    data class Default(
        val battleLogModel: BattleLogModel = BattleLogModel(2, 1, 0),
        val gameUIModel: GameUIModel = GameUIModel(
            battleId = "", opponentInfo = OpponentInfo(
                "", "", "", 0
            ),
            battleType = BattleType.BreakStone("", "", 0)
        ),
        override val loading: Boolean = false,
        val findMatching: Boolean = false,
        override val message: String = "",
    ) : BattleViewState()
}
