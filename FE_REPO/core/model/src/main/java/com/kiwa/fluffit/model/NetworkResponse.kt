package com.kiwa.fluffit.model

data class NetworkResponse<T>(
    val status: Int,
    val data: T,
    val msg: String
)
