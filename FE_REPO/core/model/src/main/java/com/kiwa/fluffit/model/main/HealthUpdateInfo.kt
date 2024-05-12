package com.kiwa.fluffit.model.main

import com.kiwa.fluffit.model.flupet.FlupetStatus

data class HealthUpdateInfo(
    val health: Int,
    val nextUpdateTime: Long,
    val isEvolutionAvailable: Boolean,
    val flupetStatus: FlupetStatus
)
