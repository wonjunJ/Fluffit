package com.ssafy.fluffitflupet.dto

import java.time.LocalDateTime

data class MainInfoDto(
    var fullness: Int,
    var health: Int,
    var flupetName: String,
    var exp: Int,
    var imageUrl: String,
    var birthDay: LocalDateTime,
    var nextFullnessUpdateTime: LocalDateTime,
    var nextHealthUpdateTime: LocalDateTime,
    var isDead: Boolean
)
