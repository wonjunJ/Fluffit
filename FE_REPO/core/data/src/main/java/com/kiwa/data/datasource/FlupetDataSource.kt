package com.kiwa.data.datasource

import com.kiwa.fluffit.model.main.FullnessUpdateInfo
import com.kiwa.fluffit.model.main.HealthUpdateInfo
import com.kiwa.fluffit.model.main.response.FlupetResponse
import com.kiwa.fluffit.model.main.response.NewEggResponse

interface FlupetDataSource {
    suspend fun fetchMainUIInfo(): Result<FlupetResponse>

    suspend fun fetchFullness(): Result<FullnessUpdateInfo>

    suspend fun fetchHealth(): Result<HealthUpdateInfo>

    suspend fun createNewEgg(): Result<NewEggResponse>
}
