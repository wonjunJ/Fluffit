package com.kiwa.fluffit.model.battle

data class BattleResultUIModel(
    val isWin: Boolean,
    val opponentBattleScore: Int,
    val myBattleScore: Int,
    val battlePoint: Int,
    val pointDiff: Int
)
