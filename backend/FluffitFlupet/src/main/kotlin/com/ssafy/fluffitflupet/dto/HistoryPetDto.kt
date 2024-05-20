package com.ssafy.fluffitflupet.dto

import java.time.LocalDate
import java.time.LocalDateTime

data class HistoryPetDto(
    var species: String,
    var name: String,
    var imageUrl: List<String>,
    var birthDay: LocalDateTime,
    var endDay: LocalDate,
    var age: String,
    var steps: Long
)
