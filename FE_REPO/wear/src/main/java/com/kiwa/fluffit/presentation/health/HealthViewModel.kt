package com.example.wearapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiwa.fluffit.presentation.api.ApiRepository
import com.kiwa.fluffit.presentation.home.HomeViewModel
import com.kiwa.fluffit.presentation.model.StepCountResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HealthViewModel"
@HiltViewModel
class HealthViewModel @Inject constructor(
    private val healthRepository: HealthRepository,
    private val apiRepository: ApiRepository
) : ViewModel() {
    private val _heartRate = MutableStateFlow<Int?>(0)
    val heartRate: StateFlow<Int?> = _heartRate.asStateFlow()

    private val _calories = MutableStateFlow<Double>(0.0)
    val calories: StateFlow<Double> = _calories.asStateFlow()

    private val _steps = MutableStateFlow<Long?>(0L)
    val steps: StateFlow<Long?> = _steps.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadHealthData()
    }

    suspend fun sendCoinRequest(): StepCountResponse? {
        val currentSteps = _steps.value ?: return null
        return try {
            val date = java.time.Instant.now().toString()
            val response: StepCountResponse? = apiRepository.sendStepCount(date, currentSteps.toInt())
            if (response != null) {
                Log.d(TAG, "요청 날짜: $date")
                Log.d(TAG, "코인 요청 Total Coin: ${response.totalCoin}, Gained Coin: ${response.gainedCoin}")
                response
            } else {
                _error.value = "Failed to fetch coin information"
                null
            }
        } catch (e: Exception) {
            _error.value = e.message
            null
        }
    }

    fun updateCaloriesSince(startTime: Long) {
        Log.d(TAG, "칼로리 계산 시작")
        viewModelScope.launch {
            try {
                _isLoading.value = true
                healthRepository.getCaloriesBurned(startTime) { calories ->
                    _calories.value = calories
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadHealthData() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                healthRepository.getDailySteps { steps ->
                    _steps.value = steps
                }
                healthRepository.startHeartRateMeasurement { heartRate ->
                    _heartRate.value = heartRate
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        healthRepository.stopHeartRateMeasurement()
    }
}
