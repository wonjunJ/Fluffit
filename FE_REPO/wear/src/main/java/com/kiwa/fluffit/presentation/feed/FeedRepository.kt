package com.kiwa.fluffit.presentation.feed

import com.kiwa.fluffit.model.UserFlupetStatus
import com.kiwa.fluffit.model.food.FoodInfo

interface FeedRepository {
    suspend fun getFoodList(): Result<FoodInfo>

    suspend fun feedFlupet(foodId: Int): Result<UserFlupetStatus>
}
