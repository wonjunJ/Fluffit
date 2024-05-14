package com.kiwa.fluffit.model.battle

data class BattleResultUIModel(
    val isWin: Boolean,
    val opponentBattleScore: String,
    val myBattleScore: String,
    val battlePoint: Int
)
