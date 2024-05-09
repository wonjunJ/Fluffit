package com.ssafy.fluffitflupet.dto

data class GenFlupetResponse(
    var flupetName: String,
    var imageUrl: String?,
    var fullness: Int,
    var health: Int
)
