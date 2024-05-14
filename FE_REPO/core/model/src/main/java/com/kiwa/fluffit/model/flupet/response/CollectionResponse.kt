package com.kiwa.fluffit.model.flupet.response

import com.google.gson.annotations.SerializedName

data class Flupets(
    @SerializedName("flupets") val flupets: List<Collections>
)

data class Collections(
    @SerializedName("species") val species: String,
    @SerializedName("imageUrl") val imageUrl: List<String>,
    @SerializedName("tier") val tier: Int,
    @SerializedName("metBefore") val metBefore: Boolean
)

data class FlupetHistory(
    @SerializedName("flupets") val flupets: List<MyFlupets>
)

data class MyFlupets(
    @SerializedName("species") val species: String,
    @SerializedName("name") val name: String,
    @SerializedName("imageUrl") val imageUrl: List<String>,
    @SerializedName("birthDay") val birthDay: String,
    @SerializedName("endDay") val endDay: String,
    @SerializedName("age") val age: String,
    @SerializedName("steps") val steps: Int
)
