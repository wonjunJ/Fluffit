package com.kiwa.fluffit.model.battle

data class BattleLog(
    val isWin: Boolean,
    val typeName: String,
    val opponentName: String,
    val opponentScore: Int,
    val myScore: Int,
    val date: String
)
