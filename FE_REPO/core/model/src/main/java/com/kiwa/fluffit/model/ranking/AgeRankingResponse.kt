package com.kiwa.fluffit.model.ranking

data class AgeRankingResponse(
    val ranking: List<AgeRankingInfo>,
    val myRank: AgeRankingInfo
)

data class AgeRankingInfo(
    val rank: Int,
    val userName: String,
    val lifeTime: Int,
    val petName: String,
    val petImageUrl: String
)
