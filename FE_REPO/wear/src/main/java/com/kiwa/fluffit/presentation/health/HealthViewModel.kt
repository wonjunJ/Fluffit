package com.example.wearapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiwa.fluffit.presentation.api.ApiRepository
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

    private val _steps = MutableStateFlow<Long?>(0L)
    val steps: StateFlow<Long?> = _steps.asStateFlow()

    private val _distance = MutableStateFlow<Double?>(0.0)
    val distance: StateFlow<Double?> = _distance.asStateFlow()

    private val _calories = MutableStateFlow<Double?>(0.0)
    val calories: StateFlow<Double?> = _calories.asStateFlow()

    private var startRunningDistance: Double? = null
    private val _runningDistance = MutableStateFlow<Double?>(null)
    val runningDistance: StateFlow<Double?> = _runningDistance.asStateFlow()

    fun getDistance() : Double{
        return _runningDistance.value ?: 0.0
    }

    fun getCalories() : Double {
        return _runningCalories.value ?: 0.0
    }

    private var startRunningCalories: Double? = null
    private val _runningCalories = MutableStateFlow<Double?>(null)
    val runningCalories: StateFlow<Double?> = _runningCalories.asStateFlow()

    private val _endRunning = MutableStateFlow(false)
    val endRunning: StateFlow<Boolean> = _endRunning.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadHealthData()
    }

    fun startRunning() {
        startRunningDistance = _distance.value
        _runningDistance.value = 0.0
        startRunningCalories = _calories.value
        _runningCalories.value = 0.0
    }

    fun turnOffResult() {
        _endRunning.value = false
    }

    fun pauseRunning() {
        startRunningDistance = null
        startRunningCalories = null
        _endRunning.value = true
    }

    fun stopRunning() {
        startRunningDistance = null
        _runningDistance.value = null
        startRunningCalories = null
        _runningCalories.value = null
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

    suspend fun sendRunningRequest(calories : Int, startTime : Long, endTime : Long) {
        val response = apiRepository.sendRunning(startTime, endTime, calories)
        if (response != null) {
            println("운동 기록이 성공적으로 전송되었습니다. 보상: ${response.reward}")
        } else {
            println("운동 기록 전송에 실패했습니다.")
        }
    }

    private fun loadHealthData() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                healthRepository.startHeartRateMeasurement { heartRate ->
                    _heartRate.value = heartRate
                }
                healthRepository.startTracking(
                    onStepsUpdated = { steps ->
                        _steps.value = steps
                    },
                    onDistanceUpdated = { distance ->
                        _distance.value = distance
                        if (startRunningDistance != null) {
                            _runningDistance.value = distance - (startRunningDistance ?: 0.0)
                        }
                    },
                    onCaloriesUpdated = { calories ->
                        _calories.value = calories
                        if (startRunningCalories != null) {
                            _runningCalories.value = calories - (startRunningCalories ?: 0.0)
                        }
                    }
                )
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
