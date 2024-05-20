package com.kiwa.fluffit.presentation.datasource

import com.kiwa.fluffit.model.UserFlupetStatus
import com.kiwa.fluffit.model.food.FoodInfo

interface FeedDataSource {
    suspend fun loadFoods(): Result<FoodInfo>

    suspend fun feedingFlupet(foodId:Int) : Result<UserFlupetStatus>
}
