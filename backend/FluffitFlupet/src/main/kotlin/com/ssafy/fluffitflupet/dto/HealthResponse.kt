package com.ssafy.fluffitflupet.dto

data class HealthResponse(
    var health: Int,
    var nextUpdateTime: Long,
    var isEvolutionAvailable: Boolean = false
) {
}