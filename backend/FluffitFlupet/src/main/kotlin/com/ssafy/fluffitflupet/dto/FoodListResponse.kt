package com.ssafy.fluffitflupet.dto

data class FoodListResponse(
    var foods: List<Food>,
    var coin: Int
){
    data class Food(
        var id: Long?,
        var name: String,
        var fullnessEffect: Int,
        var healthEffect: Int,
        var price: Int,
        var imageUrl: String,
        var description: String
    )
}
