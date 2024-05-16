package com.kiwa.fluffit.presentation.api

import com.kiwa.fluffit.presentation.model.FlupetStatus
import javax.inject.Inject

class ApiRepository @Inject constructor(private val flupetService: ApiService) {
    suspend fun fetchFlupetStatus(): FlupetStatus? {
        val response = flupetService.getFlupetStatus()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}
