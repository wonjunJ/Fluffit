package com.kiwa.fluffit.presentation.model

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class ExerciseRequest(
    val startTime: String,
    val endTime: String,
    val calorie: Int
) {
    companion object {
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

        fun fromMillis(
            startTimeInMillis: Long,
            endTimeInMillis: Long,
            calorie: Int
        ): ExerciseRequest {
            val startTime = LocalDateTime.ofEpochSecond(startTimeInMillis / 1000, 0, ZoneOffset.UTC)
                .format(formatter)
            val endTime = LocalDateTime.ofEpochSecond(endTimeInMillis / 1000, 0, ZoneOffset.UTC)
                .format(formatter)
            return ExerciseRequest(startTime, endTime, calorie)
        }
    }
}
