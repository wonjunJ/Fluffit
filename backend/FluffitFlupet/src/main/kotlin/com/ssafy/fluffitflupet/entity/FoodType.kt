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
    var healthEffect: Int,
    var price: Int,
    var stock: Int, //남아있는 음식의 재고량
    var description: String
) {
}