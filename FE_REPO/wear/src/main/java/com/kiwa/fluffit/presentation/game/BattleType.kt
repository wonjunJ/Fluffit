package com.kiwa.fluffit.presentation.game

sealed class BattleType {
    abstract val title: String
    abstract val description: String
    abstract val time: String

    data class BreakStone(
        override val title: String,
        override val description: String,
        override val time: String
    ) : BattleType()
    data class RaisingHeartBeat(
        override val title: String,
        override val description: String,
        override val time: String
    ) : BattleType()
}
