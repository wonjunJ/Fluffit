package com.kiwa.data.api

import com.kiwa.fluffit.model.ranking.AgeRankingResponse
import com.kiwa.fluffit.model.ranking.BattleRankingResponse
import retrofit2.Response
import retrofit2.http.GET

interface RankingService {

    @GET("member-service/member/battle-rank")
    suspend fun fetchBattleRanking(): Response<BattleRankingResponse>

    @GET("flupet-service/flupet/age")
    suspend fun fetchAgeRanking(): Response<AgeRankingResponse>
}
