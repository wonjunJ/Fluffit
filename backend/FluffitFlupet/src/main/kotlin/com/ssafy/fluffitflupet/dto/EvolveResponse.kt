package com.ssafy.fluffitflupet.dto

data class EvolveResponse(
    var flupetName: String,
    var imageUrl: List<String>,
    var fullness: Int,
    var health: Int,
    var isEvolutionAvailable: Boolean,
    var nextFullnessUpdateTime: Long,
    var nextHealthUpdateTime: Long
)
