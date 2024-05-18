package com.kiwa.fluffit.presentation.api

import com.kiwa.fluffit.presentation.model.ExerciseRequest
import com.kiwa.fluffit.presentation.model.ExerciseResponse
import com.kiwa.fluffit.presentation.model.FlupetStatus
import com.kiwa.fluffit.presentation.model.StepCountRequest
import com.kiwa.fluffit.presentation.model.StepCountResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @GET("/flupet-service/flupet/info ")
    suspend fun getFlupetStatus(): Response<FlupetStatus>

    @POST("/member-service/exercise/steps")
    suspend fun sendStepCount(@Body request: StepCountRequest): Response<StepCountResponse>

    @PUT("/flupet-service/flupet/pat")
    suspend fun patRequest() : Response<Unit>

    @POST("/member-service/exercise/running")
    suspend fun sendRunning(@Body request: ExerciseRequest): Response<ExerciseResponse>
}
