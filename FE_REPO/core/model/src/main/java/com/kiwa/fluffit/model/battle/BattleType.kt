package com.kiwa.fluffit.model.battle

sealed class BattleType {
    abstract val title: String
    abstract val description: String
    abstract val time: Int

    data class BreakStone(
        override val title: String,
        override val description: String,
        override val time: Int
    ) : BattleType()
    data class RaisingHeartBeat(
        override val title: String,
        override val description: String,
        override val time: Int
    ) : BattleType()
}
