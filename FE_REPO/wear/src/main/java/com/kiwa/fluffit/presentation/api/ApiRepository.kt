package com.kiwa.fluffit.presentation.api

import com.kiwa.fluffit.presentation.model.FlupetStatus
import com.kiwa.fluffit.presentation.model.StepCountRequest
import com.kiwa.fluffit.presentation.model.StepCountResponse
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun fetchFlupetStatus(): FlupetStatus? {
        val response = apiService.getFlupetStatus()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun sendStepCount(date: String, stepCount: Int): StepCountResponse? {
        val request = StepCountRequest(date, stepCount)
        val response = apiService.sendStepCount(request)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun patRequest() : Boolean {
        val response = apiService.patRequest()
        return if (response.isSuccessful) {
            true
        } else {
            false
        }
    }
}
