package com.kiwa.data.datasource

import com.kiwa.fluffit.model.NetworkResponse
import com.kiwa.fluffit.model.main.MainUIModel

interface MainDataSource {
    suspend fun fetchMainUIInfo(): Result<NetworkResponse<MainUIModel>>
}
