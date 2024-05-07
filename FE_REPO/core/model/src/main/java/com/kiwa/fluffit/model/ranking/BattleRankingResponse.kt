package com.kiwa.fluffit.model.ranking

data class BattleRankingResponse(
    val ranking: List<BattleRankingInfo>,
    val myRank: BattleRankingInfo
)

data class BattleRankingInfo(
    val rank: Int,
    val userName: String,
    val battlePoint: Int,
    val petName: String,
    val petImageUrl: String
)
