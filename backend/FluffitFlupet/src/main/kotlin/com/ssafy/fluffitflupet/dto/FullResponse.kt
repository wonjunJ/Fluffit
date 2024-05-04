package com.ssafy.fluffitflupet.dto

data class FullResponse(
    var fullness: Int,
    var nextUpdateTime: Long,
    var isEvolutionAvailable: Boolean = false
)
