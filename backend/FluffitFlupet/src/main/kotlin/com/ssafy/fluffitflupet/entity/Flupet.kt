package com.ssafy.fluffitflupet.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("flupet")
data class Flupet(
    @Id
    val id: Long? = null,
    var name: String,
    var imgUrl: String,
    var stage: Int
) {
}