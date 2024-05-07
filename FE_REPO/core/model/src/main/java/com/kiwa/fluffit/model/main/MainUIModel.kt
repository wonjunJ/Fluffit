package com.kiwa.fluffit.model.main

data class MainUIModel(
    val coin: Int,
    val flupet: Flupet,
    val nextFullnessUpdateTime: Long,
    val nextHealthUpdateTime: Long,
)
