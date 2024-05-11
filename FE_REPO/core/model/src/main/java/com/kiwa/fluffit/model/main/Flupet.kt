package com.kiwa.fluffit.model.main

data class Flupet(
    val fullness: Int,
    val health: Int,
    val imageUrls: ImageUrls,
    val name: String,
    val birthDay: String,
    val age: String,
    val evolutionAvailable: Boolean
)
