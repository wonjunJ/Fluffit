package com.kiwa.fluffit.model.main

import com.kiwa.fluffit.model.flupet.FlupetStatus

data class MainUIModel(
    val coin: Int,
    val flupet: Flupet,
    val nextFullnessUpdateTime: Long,
    val nextHealthUpdateTime: Long,
    val flupetStatus: FlupetStatus
)
