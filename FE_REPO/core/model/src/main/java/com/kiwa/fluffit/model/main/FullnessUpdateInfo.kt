package com.kiwa.fluffit.model.main

import com.kiwa.fluffit.model.flupet.FlupetStatus

data class FullnessUpdateInfo(
    val fullness: Int,
    val nextUpdateTime: Long,
    val isEvolutionAvailable: Boolean,
    val flupetStatus: FlupetStatus
)
