package com.kiwa.fluffit.presentation.api

import android.util.Log
import com.kiwa.fluffit.presentation.model.ExerciseRequest
import com.kiwa.fluffit.presentation.model.ExerciseResponse
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

    suspend fun sendRunning(startTimeInMillis: Long, endTimeInMillis: Long, calorie: Int): ExerciseResponse? {
        val request = ExerciseRequest.fromMillis(startTimeInMillis, endTimeInMillis, calorie)
        val response = apiService.sendRunning(request)

        Log.d("TAG", "운동요청 : startTime: $startTimeInMillis, endTime: $endTimeInMillis, cal: $calorie")
        return if (response.isSuccessful) {
            response.body()
        } else {
            val errorMsg = response.errorBody()?.string() ?: "Unknown error"
            Log.d("TAG", "운동 요청 실패: $errorMsg")
            null
        }
    }

    suspend fun patRequest() : Boolean {
        val response = apiService.patRequest()
        return if (response.isSuccessful) {
            true
        } else {
            Log.d("API", "쓰다듬기 실패${response.body()}")
            false
        }
    }
}
