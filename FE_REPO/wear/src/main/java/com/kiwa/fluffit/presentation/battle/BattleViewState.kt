package com.kiwa.fluffit.presentation.battle

import com.kiwa.fluffit.base.ViewState
import com.kiwa.fluffit.model.battle.BattleLogModel
import com.kiwa.fluffit.model.battle.OpponentInfo

interface BattleInfo {
    val battleId: String
    val battleType: BattleType
}

sealed class BattleViewState : ViewState {

    abstract val loading: Boolean

    data class Default(
        val battleLogModel: BattleLogModel = BattleLogModel(2, 1, 0),
        override val loading: Boolean = false,
        val findMatching: Boolean = false
    ) : BattleViewState()

    data class MatchingCompleted(
        val opponentInfo: OpponentInfo = OpponentInfo(
            "", "", "", 0
        ),
        override val battleId: String,
        override val battleType: BattleType,
        override val loading: Boolean,
    ) : BattleViewState(), BattleInfo

    data class Battle(
        override val battleType: BattleType,
        val score: Int, override val loading: Boolean, override val battleId: String
    ) : BattleViewState(), BattleInfo

    data class BattleResult(
        val fightResult: String = "", override val loading: Boolean,
    ) : BattleViewState()

}
