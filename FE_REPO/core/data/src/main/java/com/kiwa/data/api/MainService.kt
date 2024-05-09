package com.kiwa.data.api

import com.kiwa.fluffit.model.NetworkResponse
import com.kiwa.fluffit.model.main.MainUIModel
import retrofit2.http.GET

interface MainService {
    @GET("main/flupet/info")
    suspend fun fetchMainUIInfo(): NetworkResponse<MainUIModel>
}
