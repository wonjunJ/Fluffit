package com.kiwa.fluffit.presentation.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.lang.System.currentTimeMillis
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor() : ViewModel() {
    private val _startTime = MutableStateFlow<Long?>(null)
    private val _endTime = MutableStateFlow<Long?>(null)
    private val _isTimerRunning = MutableStateFlow(false)
    private val _timerValue = MutableStateFlow("00:00.00")
    private val _elapsedTime = MutableStateFlow(0L) // 누적 시간

    val startTime: StateFlow<Long?> = _startTime.asStateFlow()
    val endTime: StateFlow<Long?> = _endTime.asStateFlow()
    val timerValue: StateFlow<String> = _timerValue.asStateFlow()
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    fun startTimer() {
        if (!_isTimerRunning.value) {
            _startTime.value = currentTimeMillis() - _elapsedTime.value
            _isTimerRunning.value = true
            viewModelScope.launch {
                while (_isTimerRunning.value) {
                    val currentTime = currentTimeMillis()
                    _startTime.value?.let {
                        val timeDiff = currentTime - it
                        _elapsedTime.value = timeDiff
                        _timerValue.value = formatTime(timeDiff)
                    }
                    delay(10)  // 10 밀리초마다 업데이트
                }
            }
        }
    }

    fun pauseTimer() {
        _isTimerRunning.value = false
        _endTime.value = currentTimeMillis()
    }

    fun resetTimer() {
        pauseTimer()
        _elapsedTime.value = 0
        _timerValue.value = "00:00.00"
    }

    private fun formatTime(millis: Long): String {
        val minutes = (millis / 60000) % 60  // 분
        val seconds = (millis / 1000) % 60  // 초
        val centiseconds = (millis / 10) % 100  // 백의 자리 밀리초
        return String.format("%02d:%02d.%02d", minutes, seconds, centiseconds)
    }
}
