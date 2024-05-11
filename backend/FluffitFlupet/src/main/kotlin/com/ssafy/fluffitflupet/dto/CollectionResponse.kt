package com.ssafy.fluffitflupet.dto

data class CollectionResponse(
    var flupets: List<Flupet>
){
    data class Flupet(
        var species: String,
        var imageUrl: List<String>,
        var tier: Int,
        var metBefore: Boolean //유저가 이 플러펫을 가지고 있는지 여부
    )
}
