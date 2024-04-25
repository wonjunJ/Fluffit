package com.ssafy.fluffitflupet.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("food_type")
data class FoodType(
    @Id
    val id: Long? = null,
    var name: String,
    var imgUrl: String,
    var fullnessEffect: Int,
    var health_effect: Int,
    var price: Int,
    var description: String
) {
}