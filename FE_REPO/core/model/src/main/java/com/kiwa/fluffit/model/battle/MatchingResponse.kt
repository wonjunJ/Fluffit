package com.kiwa.fluffit.model.battle

data class MatchingResponse(
    val result: Boolean,
    val opponentName: String,
    val opponentFlupetName: String,
    val opponentFlupetImageUrl: String,
    val opponentBattlePoint: Int,
    val battleId: String,
    val battleTypeResponse: BattleTypeResponse
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
        battleType = when (this.battleTypeResponse.key) {
            "BATTLE_ROCK" -> BattleType.BreakStone(
                title = this.battleTypeResponse.title,
                description = this.battleTypeResponse.description,
                time = this.battleTypeResponse.time
            )

            else -> BattleType.RaisingHeartBeat(
                title = this.battleTypeResponse.title,
                description = this.battleTypeResponse.description,
                time = this.battleTypeResponse.time
            )
        }
    )
