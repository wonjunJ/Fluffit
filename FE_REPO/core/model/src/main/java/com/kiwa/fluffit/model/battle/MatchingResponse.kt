package com.kiwa.fluffit.model.battle

data class MatchingResponse(
    val result: Boolean,
    val opponentName: String,
    val opponentFlupetName: String,
    val opponentFlupetImageUrl: String,
    val opponentBattlePoint: Int,
    val battleId: String,
    val battleType: BattleTypeResponse
)

data class BattleTypeResponse(
    val key: String,
    val title: String,
    val time: Int,
    val description: String
)

fun MatchingResponse.toGameUIModel() =
    GameUIModel(
        battleId = this.battleId,
        opponentInfo = OpponentInfo(
            opponentName = this.opponentName,
            opponentFlupetName = this.opponentFlupetName,
            opponentFlupetImage = this.opponentFlupetImageUrl,
            opponentBattlePoint = this.opponentBattlePoint
        ),
        key = this.battleType.key,
        title = this.battleType.title,
        description = this.battleType.description,
        time = this.battleType.time
    )
