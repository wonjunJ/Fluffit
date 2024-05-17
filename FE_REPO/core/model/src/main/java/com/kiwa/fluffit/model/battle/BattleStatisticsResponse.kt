package com.kiwa.fluffit.model.battle

data class BattleStatisticsResponse(
    val battlePoint: Int,
    val battleStatisticItemDtoList: List<GameStatistics>
)

data class GameStatistics(
    val title: String,
    val totalCount: Int,
    val winCount: Int,
    val loseCount: Int,
    val winRate: Double
)

fun BattleStatisticsResponse.toBattleStatisticsUIModel(): BattleStatisticsUIModel {
    val (totalWin, totalGameCount, totalLose) = this.battleStatisticItemDtoList
        .takeIf { it.isNotEmpty() }
        ?.run {
            Triple(sumOf { it.winCount }, sumOf { it.totalCount }, sumOf { it.loseCount })
        } ?: Triple(0, 0, 0)

    val totalWinRate = if (totalGameCount > 0) {
        Math.round((totalWin.toDouble() / totalGameCount) * 1000) / 10.0
    } else {
        0.0
    }
    return BattleStatisticsUIModel(
        totalWin = totalWin,
        totalGameCount = totalGameCount,
        totalLose = totalLose,
        totalWinRate = totalWinRate,
        battlePoint = this.battlePoint
    )
}
