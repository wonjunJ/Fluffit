package com.kiwa.fluffit.model.battle

data class GameUIModel(
    val battleId: String,
    val opponentInfo: OpponentInfo,
    val key: String,
    val title: String,
    val description: String,
    val time: Int
)
