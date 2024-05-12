package com.kiwa.fluffit.model.user.request

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @SerializedName("nickname") val nickname: String
)
