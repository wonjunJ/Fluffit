package com.kiwa.fluffit.model.flupet

data class Flupet(
    val fullness: Int,
    val health: Int,
    val imageUrl: String,
    val name: String
)

data class FlupetCollection(
    val species: String,
    val imageUrl: String,
    val tier: Int,
    val metBefore: Boolean
)
