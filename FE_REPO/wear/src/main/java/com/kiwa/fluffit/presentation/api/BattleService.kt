package com.kiwa.fluffit.presentation.api

import com.kiwa.fluffit.model.battle.BattleStatisticsResponse
import retrofit2.Response
import retrofit2.http.GET

interface BattleService {

    @GET("battle-service/statistics")
    suspend fun getBattleStatistics(): Response<BattleStatisticsResponse>

    suspend fun cancelBattle(): Response<Unit>
}
