package com.kiwa.fluffit.presentation.api

import com.kiwa.fluffit.presentation.model.FlupetStatus
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("/flupet-service/flupet/info ")
    suspend fun getFlupetStatus(): Response<FlupetStatus>
}
