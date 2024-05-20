package com.ssafy.fluffitflupet.repository

import com.ssafy.fluffitflupet.entity.FoodType
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface FoodRepository: CoroutineCrudRepository<FoodType, Long> {
}