package com.kiwa.fluffit.model.user.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("nickname") val nickName: String
)

data class UserModificationResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("msg") val resultCode: String
)
