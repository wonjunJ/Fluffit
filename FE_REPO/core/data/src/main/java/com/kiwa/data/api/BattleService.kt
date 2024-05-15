package com.kiwa.data.api

interface BattleService {
    suspend fun getBattleLogs()

    suspend fun getBattleStatistics()

    suspend fun cancelBattle()
}
