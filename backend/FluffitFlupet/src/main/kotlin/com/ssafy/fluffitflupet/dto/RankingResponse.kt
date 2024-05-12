package com.ssafy.fluffitflupet.dto

data class RankingResponse(
    var ranking: List<PetAgeInfo>,
    var myRank: PetAgeInfo
) {
    data class PetAgeInfo(
        var rank: Int,
        var userNickname: String = "",
        var lifetime: Long,
        var flupetNickname: String,
        var imageUrl: List<String>
    )
}