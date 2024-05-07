package com.kiwa.fluffit.model.flupet.response

data class CollectionResponse(
    val status: Int,
    val msg: String,
    val data: Flupets
)

data class Flupets(
    val flupets: List<Collections>
)

data class Collections(
    val species: String,
    val imageUrl: String,
    val tier: Int,
    val metBefore: Boolean
)
