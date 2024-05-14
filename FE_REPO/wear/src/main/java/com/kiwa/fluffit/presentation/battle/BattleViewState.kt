package com.kiwa.fluffit.presentation.battle

import com.kiwa.fluffit.base.ViewState
import com.kiwa.fluffit.model.battle.BattleLogModel
import com.kiwa.fluffit.model.battle.OpponentInfo

sealed class BattleViewState : ViewState {

    abstract val loading: Boolean

    data class Default(
        val battleLogModel: BattleLogModel = BattleLogModel(2, 1, 0),
        val opponentInfo: OpponentInfo = OpponentInfo(
            "", "", "", 0
        ),
        override val loading: Boolean = false,
        val findMatching: Boolean = false
    ) : BattleViewState()
}
