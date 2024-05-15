package com.kiwa.fluffit.model.battle

data class GameUIModel(
    val battleId: String,
    val opponentInfo: OpponentInfo,
    val battleType: BattleType
)
