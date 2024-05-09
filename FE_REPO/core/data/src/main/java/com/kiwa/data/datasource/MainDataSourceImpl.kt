package com.kiwa.data.datasource

import com.kiwa.data.api.MainService
import com.kiwa.fluffit.model.NetworkResponse
import com.kiwa.fluffit.model.main.FullnessUpdateInfo
import com.kiwa.fluffit.model.main.HealthUpdateInfo
import com.kiwa.fluffit.model.main.MainUIModel
import javax.inject.Inject

class MainDataSourceImpl @Inject constructor(
    private val mainService: MainService
) : MainDataSource {
    override suspend fun fetchMainUIInfo(): Result<NetworkResponse<MainUIModel>> =
        runCatching { mainService.fetchMainUIInfo() }

    override suspend fun fetchFullness(): Result<NetworkResponse<FullnessUpdateInfo>> =
        runCatching { mainService.fetchFullnessInfo() }

    override suspend fun fetchHealth(): Result<NetworkResponse<HealthUpdateInfo>> =
        runCatching { mainService.fetchHealthInfo() }
}
