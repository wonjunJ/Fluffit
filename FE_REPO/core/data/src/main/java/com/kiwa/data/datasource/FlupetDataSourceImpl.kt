package com.kiwa.data.datasource

import com.kiwa.data.api.FlupetService
import com.kiwa.data.model.FlupetStatusException
import com.kiwa.fluffit.model.main.FullnessUpdateInfo
import com.kiwa.fluffit.model.main.HealthUpdateInfo
import com.kiwa.fluffit.model.main.response.FlupetResponse
import com.kiwa.fluffit.model.main.response.NewEggResponse
import javax.inject.Inject

class FlupetDataSourceImpl @Inject constructor(
    private val flupetService: FlupetService
) : FlupetDataSource {
    override suspend fun fetchMainUIInfo(): Result<FlupetResponse> =
        try {
            val response = flupetService.fetchMainUIInfo()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 에러"))
        }

    override suspend fun fetchFullness(): Result<FullnessUpdateInfo> =
        try {
            val response = flupetService.fetchFullnessInfo()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(FlupetStatusException.FlupetIsDead(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 에러"))
        }

    override suspend fun fetchHealth(): Result<HealthUpdateInfo> =
        try {
            val response = flupetService.fetchHealthInfo()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(FlupetStatusException.FlupetIsDead(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun createNewEgg(): Result<NewEggResponse> =
        try {
            val response = flupetService.createNewEgg()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 에러"))
        }
}
