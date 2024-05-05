package com.ssafy.fluffitflupet.dto

import java.time.LocalDate

data class MainInfoResponse(
    var fullness: Int,
    var health: Int,
    var flupetName: String,
    var imageUrl: String,
    var birthDay: LocalDate,
    var age: String,
    var isEvolutionAvailable: Boolean = false,
    var nextFullnessUpdateTime: Long,
    var nextHealthUpdateTime: Long
)
