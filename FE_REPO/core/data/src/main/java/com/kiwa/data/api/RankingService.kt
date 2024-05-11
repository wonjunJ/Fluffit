package com.kiwa.data.api

import com.kiwa.fluffit.model.ranking.AgeRankingResponse
import com.kiwa.fluffit.model.ranking.BattleRankingResponse
import retrofit2.Response
import retrofit2.http.GET

interface RankingService {

    @GET("ranking/battle")
    suspend fun fetchBattleRanking(): Response<BattleRankingResponse>

    @GET("ranking/age")
    suspend fun fetchAgeRanking(): Response<AgeRankingResponse>
}
