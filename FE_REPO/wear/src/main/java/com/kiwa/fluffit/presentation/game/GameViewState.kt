package com.kiwa.fluffit.presentation.game

import com.kiwa.fluffit.base.ViewState
import com.kiwa.fluffit.model.battle.BattleResultUIModel

interface BattleInfo {
    val battleId: String
    val battleType: BattleType
}

sealed class GameViewState : ViewState {

    abstract val loading: Boolean

    data class MatchingCompleted(
        override val battleId: String = "",
        override val battleType: BattleType = BattleType.BreakStone("", "", ""),
        override val loading: Boolean = false,
    ) : GameViewState(), BattleInfo

    data class Battle(
        override val battleType: BattleType,
        val score: Int, override val loading: Boolean, override val battleId: String
    ) : GameViewState(), BattleInfo

    data class BattleResult(
        val result: BattleResultUIModel, override val loading: Boolean,
    ) : GameViewState()
}
