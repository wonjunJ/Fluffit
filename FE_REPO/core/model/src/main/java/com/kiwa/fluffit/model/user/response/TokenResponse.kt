package com.kiwa.fluffit.model.user.response

data class AutoLoginResponse(
    val status: Int,
    val message: String
)

data class Tokens(
    val accessToken: String,
    val refreshToken: String
)
