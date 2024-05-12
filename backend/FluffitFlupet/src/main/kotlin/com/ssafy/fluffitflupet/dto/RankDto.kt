package com.ssafy.fluffitflupet.dto

data class RankDto(
    var rank: Int,
    var userNickname: String = "",
    var userId: String,
    var lifetime: Long,
    var flupetNickname: String,
    var imageUrl: List<String>
)
