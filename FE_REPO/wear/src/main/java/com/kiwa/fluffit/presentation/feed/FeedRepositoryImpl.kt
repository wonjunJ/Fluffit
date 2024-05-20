package com.kiwa.fluffit.presentation.feed

import com.kiwa.fluffit.model.UserFlupetStatus
import com.kiwa.fluffit.model.food.FoodInfo
import com.kiwa.fluffit.presentation.datasource.FeedDataSource
import javax.inject.Inject

class FeedRepositoryImpl @Inject constructor(
    private val feedDataSource: FeedDataSource
) : FeedRepository {
    override suspend fun getFoodList(): Result<FoodInfo> =
        feedDataSource.loadFoods().fold(
            onSuccess = { response ->
                Result.success(response)
            },
            onFailure = {
                Result.failure(it)
            }
        )

    override suspend fun feedFlupet(foodId: Int): Result<UserFlupetStatus> =
        feedDataSource.feedingFlupet(foodId).fold(
            onSuccess = { response ->
                Result.success(response)
            },
            onFailure = {
                Result.failure(it)
            }
        )
}
