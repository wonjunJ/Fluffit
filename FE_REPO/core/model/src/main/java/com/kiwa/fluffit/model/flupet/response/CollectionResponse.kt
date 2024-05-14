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
