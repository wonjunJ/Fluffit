package com.ssafy.fluffitflupet.dto

import java.time.LocalDate

data class MainInfoResponse(
    var fullness: Int = 0,
    var health: Int = 0,
    var flupetName: String = "",
    var imageUrl: List<String> = listOf(),
    var birthDay: String = "",
    var age: String = "",
    var isEvolutionAvailable: Boolean = false,
    var nextFullnessUpdateTime: Long = 0,
    var nextHealthUpdateTime: Long = 0,
    var coin: Int = 0
)
