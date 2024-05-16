package com.kiwa.fluffit.presentation.api

interface BattleService {
    suspend fun getBattleLogs()

    suspend fun getBattleStatistics()

    suspend fun cancelBattle()
}
