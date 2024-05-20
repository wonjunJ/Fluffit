package com.kiwa.fluffit.presentation.datasource

import com.kiwa.fluffit.model.UserFlupetStatus
import com.kiwa.fluffit.model.food.FoodInfo
import com.kiwa.fluffit.presentation.api.FeedService
import javax.inject.Inject

class FeedDataSourceImpl @Inject constructor(
    private val feedService: FeedService
) : FeedDataSource {
    override suspend fun loadFoods(): Result<FoodInfo> =
        runCatching {
            feedService.loadFoods()
        }

    override suspend fun feedingFlupet(foodId: Int): Result<UserFlupetStatus> =
        runCatching {
            feedService.feedingFlupet(foodId)
        }
}
