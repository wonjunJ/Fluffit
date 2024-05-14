package com.example.wearapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HealthViewModel"
@HiltViewModel
class HealthViewModel @Inject constructor(
    private val healthRepository: HealthRepository
) : ViewModel() {
    private val _heartRate = MutableStateFlow<Int?>(0)
    val heartRate: StateFlow<Int?> = _heartRate.asStateFlow()

    private val _steps = MutableStateFlow<Long?>(0L)
    val steps: StateFlow<Long?> = _steps.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadHealthData()
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
