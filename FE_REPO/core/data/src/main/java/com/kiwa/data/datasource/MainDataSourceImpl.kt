package com.kiwa.data.datasource

import com.kiwa.data.api.MainService
import com.kiwa.fluffit.model.NetworkResponse
import com.kiwa.fluffit.model.main.MainUIModel
import javax.inject.Inject

class MainDataSourceImpl @Inject constructor(
    private val mainService: MainService
) : MainDataSource {
    override suspend fun fetchMainUIInfo(): Result<NetworkResponse<MainUIModel>> =
        runCatching { mainService.fetchMainUIInfo() }
}
