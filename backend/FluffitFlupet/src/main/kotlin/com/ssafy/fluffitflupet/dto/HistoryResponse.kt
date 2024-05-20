package com.ssafy.fluffitflupet.dto

import java.time.LocalDate

data class HistoryResponse(
    var flupets: List<MyPet> = listOf()
){
    data class MyPet(
        var species: String,
        var name: String,
        var imageUrl: List<String>,
        var birthDay: LocalDate,
        var endDay: LocalDate,
        var age: String,
        var steps: Long
    )
}
