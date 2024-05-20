package com.kiwa.fluffit.model.ranking

data class AgeRankingResponse(
    val ranking: List<AgeRankingInfo>,
    val myRank: AgeRankingInfo
)

data class AgeRankingInfo(
    val rank: Int,
    val userNickname: String,
    val lifetime: Int,
    val flupetNickname: String,
    val imageUrl: List<String>
)
