package com.kiwa.data.datasource

import com.kiwa.fluffit.model.NetworkResponse
import com.kiwa.fluffit.model.main.FullnessUpdateInfo
import com.kiwa.fluffit.model.main.HealthUpdateInfo
import com.kiwa.fluffit.model.main.MainUIModel

interface MainDataSource {
    suspend fun fetchMainUIInfo(): Result<NetworkResponse<MainUIModel>>

    suspend fun fetchFullness(): Result<NetworkResponse<FullnessUpdateInfo>>

    suspend fun fetchHealth(): Result<NetworkResponse<HealthUpdateInfo>>
}
