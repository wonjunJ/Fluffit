package com.kiwa.fluffit.presentation.model

data class FlupetStatus(
    val fullness: Int,
    val health: Int,
    val flupetName: String,
    val imageUrl: List<String>,
    val birthDay: String,
    val age: String,
    val isEvolutionAvailable: Boolean,
    val nextFullnessUpdateTime: Long,
    val nextHealthUpdateTime: Long,
    val coin: Int
)
