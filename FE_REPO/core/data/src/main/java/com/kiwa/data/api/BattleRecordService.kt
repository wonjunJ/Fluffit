package com.kiwa.data.api

import com.kiwa.fluffit.model.UserBattleHistory
import com.kiwa.fluffit.model.UserBattleStatistics
import retrofit2.http.GET

interface BattleRecordService {

    @GET("battle-service/history")
    suspend fun loadUserBattleRecord() : UserBattleHistory

    @GET("battle-service/statistics")
    suspend fun loadUserBattleStatistics() : UserBattleStatistics
}
