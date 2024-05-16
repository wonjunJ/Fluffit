package com.kiwa.fluffit.model.battle

class BattleResultResponse (
    val isWin: Boolean,
    val opponentBattleScore: Int,
    val myBattleScore: Int,
    val battlePoint: Int,
    val battlePointChanges: Int,
)
