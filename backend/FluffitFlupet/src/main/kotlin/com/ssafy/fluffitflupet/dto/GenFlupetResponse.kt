package com.ssafy.fluffitflupet.dto

data class GenFlupetResponse(
    var flupetName: String,
    var imageUrl: List<String>,
    var fullness: Int,
    var health: Int,
    var nextFullnessUpdateTime: Long = 0,
    var nextHealthUpdateTime: Long = 0
)
