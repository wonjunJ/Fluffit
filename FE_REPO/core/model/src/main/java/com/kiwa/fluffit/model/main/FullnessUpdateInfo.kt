package com.kiwa.fluffit.model.main

data class FullnessUpdateInfo(
    val fullness: Int,
    val nextUpdateTime: Long,
    val isEvolutionAvailable: Boolean
)
