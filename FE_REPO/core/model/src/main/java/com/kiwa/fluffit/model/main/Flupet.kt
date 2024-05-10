package com.kiwa.fluffit.model.main

data class Flupet(
    val fullness: Int,
    val health: Int,
    val imageUrl: String,
    val name: String,
    val birthDay: String,
    val age: String,
    val evolutionAvailable: Boolean
)

data class FlupetCollection(
    val species: String,
    val imageUrl: String,
    val tier: Int,
    val metBefore: Boolean
)
