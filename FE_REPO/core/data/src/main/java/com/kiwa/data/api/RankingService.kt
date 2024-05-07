package com.kiwa.data.api

import com.kiwa.fluffit.model.NetworkResponse
import com.kiwa.fluffit.model.ranking.AgeRankingResponse
import com.kiwa.fluffit.model.ranking.BattleRankingResponse
import retrofit2.http.GET

interface RankingService {

    @GET("ranking/battle")
    suspend fun fetchBattleRanking(): NetworkResponse<BattleRankingResponse>

    @GET("ranking/age")
    suspend fun fetchAgeRanking(): NetworkResponse<AgeRankingResponse>
}
