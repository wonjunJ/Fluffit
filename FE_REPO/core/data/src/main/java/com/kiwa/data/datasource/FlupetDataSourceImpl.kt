package com.kiwa.data.datasource

import com.kiwa.data.api.FlupetService
import com.kiwa.data.model.FlupetStatusException
import com.kiwa.fluffit.model.flupet.NicknameRequest
import com.kiwa.fluffit.model.flupet.response.BasicResponse
import com.kiwa.fluffit.model.main.FullnessUpdateInfo
import com.kiwa.fluffit.model.main.HealthUpdateInfo
import com.kiwa.fluffit.model.main.response.FlupetResponse
import com.kiwa.fluffit.model.main.response.NewEggResponse
import org.json.JSONObject
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
                val errorBodyStr = response.errorBody()?.string()
                val errorMsg = JSONObject(errorBodyStr.toString()).getString("msg")
                Result.failure(Exception(errorMsg))
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
                val errorBodyStr = response.errorBody()?.string()
                val errorMsg = JSONObject(errorBodyStr.toString()).getString("msg")
                Result.failure(FlupetStatusException.FlupetIsDead(errorMsg))
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
                val errorBodyStr = response.errorBody()?.string()
                val errorMsg = JSONObject(errorBodyStr.toString()).getString("msg")
                Result.failure(FlupetStatusException.FlupetIsDead(errorMsg))
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
                val errorBodyStr = response.errorBody()?.string()
                val errorMsg = JSONObject(errorBodyStr.toString()).getString("msg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 에러"))
        }

    override suspend fun editFlupetNickname(nickname: String): Result<BasicResponse> = try {
        val response = flupetService.editFlupetNickname(NicknameRequest(nickname))
        if (response.isSuccessful) {
            Result.success(response.body()!!)
        } else {
            val errorBodyStr = response.errorBody()?.string()
            val errorMsg = JSONObject(errorBodyStr.toString()).getString("msg")
            Result.failure(Exception(errorMsg))
        }
    } catch (e: Exception) {
        Result.failure(Exception("네트워크 에러"))
    }
}
