package com.kiwa.fluffit.model.food

import com.google.gson.annotations.SerializedName

data class FoodInfo(
    @SerializedName("foods") val foods: List<Feed>,
    @SerializedName("coin") val coin: Int
)

data class Feed(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("fullnessEffect") val fullness: Int,
    @SerializedName("healthEffect") val health: Int,
    @SerializedName("price") val price: Int,
    @SerializedName("imageUrl") val imgUrl: String,
    @SerializedName("description") val info: String,
)

