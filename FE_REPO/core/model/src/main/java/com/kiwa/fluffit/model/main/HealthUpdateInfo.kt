package com.kiwa.fluffit.model.main

data class HealthUpdateInfo(
    val health: Int,
    val nextUpdateTime: Long,
    val isEvolutionAvailable: Boolean
)
