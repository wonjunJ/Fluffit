package com.kiwa.fluffit.model.battle

data class BattleTypeLog(
    val id: Long,
    val name: String,
    val totalCount: Int,
    val winCount: Int,
    val loseCount: Int,
    val winRate: Double
)
