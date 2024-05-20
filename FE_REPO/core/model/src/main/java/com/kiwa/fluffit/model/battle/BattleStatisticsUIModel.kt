package com.kiwa.fluffit.model.battle

data class BattleStatisticsUIModel(
    val totalWin: Int,
    val totalGameCount: Int,
    val totalLose: Int,
    val totalWinRate: Double,
    val battlePoint: Int
)
