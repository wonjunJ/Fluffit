package com.kiwa.fluffit.presentation.util

internal fun Long.formatTime(): String {
    val minutes = (this / 60000) % 60  // 분
    val seconds = (this / 1000) % 60  // 초
    val centiseconds = (this / 10) % 100  // 백의 자리 밀리초
    return String.format("%02d:%02d.%02d", minutes, seconds, centiseconds)
}
