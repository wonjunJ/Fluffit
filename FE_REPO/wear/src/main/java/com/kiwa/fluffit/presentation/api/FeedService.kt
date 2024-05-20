package com.kiwa.fluffit.presentation.api

import com.kiwa.fluffit.model.UserFlupetStatus
import com.kiwa.fluffit.model.food.FoodInfo
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface FeedService {
    @GET("flupet-service/food/list")
    suspend fun loadFoods(): FoodInfo

    @PUT("flupet-service/food/feeding/{foodId}")
    suspend fun feedingFlupet(
        @Path(value = "foodId") foodId: Int
    ): UserFlupetStatus
}
