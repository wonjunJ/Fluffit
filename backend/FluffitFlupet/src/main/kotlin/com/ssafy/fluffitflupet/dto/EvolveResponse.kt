package com.ssafy.fluffitflupet.dto

data class EvolveResponse(
    var flupetName: String,
    var imageUrl: String?,
    var fullness: Int,
    var health: Int,
    var isEvolutionAvailable: Boolean,
    var nextFullnessUpdateTime: Long,
    var nextHealthUpdateTime: Long
)
