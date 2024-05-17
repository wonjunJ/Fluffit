package com.kiwa.fluffit.model

data class User(
    val userName: String
)

data class UserFlupetStatus(
    val totalCoin: Int,
    val fullnessEffect: Int,
    val healthEffect: Int
)

data class UserBattleHistory(
    val content: List<BattleContents>,
    val hasNext: Boolean
)

data class BattleContents(
    val isWin: Boolean,
    val title: String,
    val opponentName: String,
    val opponentScore: Int,
    val myScore: Int,
    val date: String
)

data class UserBattleStatistics(
    val battlePoint: Int,
    val battleStatisticItemDtoList: List<BattleStaticItemDtoList>
)

data class BattleStaticItemDtoList(
    val title: String,
    val totalCount: Int,
    val winCount: Int,
    val loseCount: Int,
    val winRate: Double
)
