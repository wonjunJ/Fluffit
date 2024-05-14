package com.kiwa.domain.repository

import com.kiwa.fluffit.model.flupet.response.BasicResponse
import com.kiwa.fluffit.model.flupet.response.MyFlupets
import com.kiwa.fluffit.model.main.FullnessUpdateInfo
import com.kiwa.fluffit.model.main.HealthUpdateInfo
import com.kiwa.fluffit.model.main.MainUIModel

interface FlupetRepository {
    suspend fun getMainUIInfo(): Result<MainUIModel>

    suspend fun updateFullness(): Result<FullnessUpdateInfo>
    suspend fun updateHealth(): Result<HealthUpdateInfo>

    suspend fun getNewEgg(): Result<MainUIModel>

    suspend fun editFlupetNickname(nickname: String): Result<BasicResponse>

    suspend fun getHistory(): Result<List<MyFlupets>>
}
