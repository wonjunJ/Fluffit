package com.kiwa.data.datasource

import android.util.Log
import com.kiwa.data.api.FlupetService
import com.kiwa.fluffit.model.main.FullnessUpdateInfo
import com.kiwa.fluffit.model.main.HealthUpdateInfo
import com.kiwa.fluffit.model.main.response.FlupetResponse
import javax.inject.Inject

class MainDataSourceImpl @Inject constructor(
    private val flupetService: FlupetService
) : MainDataSource {
    override suspend fun fetchMainUIInfo(): Result<FlupetResponse> =
        try {
            val response = flupetService.fetchMainUIInfo()
            if (response.isSuccessful) {
                Log.d("확인", response.body()!!.toString())
                Result.success(response.body()!!)
            } else {
                Log.d("확인", response.body()!!.toString())
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Log.d("확인", e.message.toString())
            Result.failure(Exception("네트워크 에러"))
        }

    override suspend fun fetchFullness(): Result<FullnessUpdateInfo> =
        try {
            val response = flupetService.fetchFullnessInfo()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.message()))
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
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
}
