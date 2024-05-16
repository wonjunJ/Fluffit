package com.kiwa.fluffit.model.ranking

data class BattleRankingResponse(
    val ranking: List<BattleRankingInfo>,
    val myRank: BattleRankingInfo
)

data class BattleRankingInfo(
    val rank: Int,
    val userNickname: String,
    val battlePoint: Int,
    val flupetNickname: String,
    val flupetImageUrl: List<String>
)
