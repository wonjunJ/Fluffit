package com.kiwa.fluffit.model.main.response

import com.kiwa.fluffit.model.main.Flupet
import com.kiwa.fluffit.model.main.ImageUrls
import com.kiwa.fluffit.model.main.MainUIModel

data class FlupetResponse(
    val fullness: Int,
    val health: Int,
    val flupetName: String,
    val imageUrl: List<String>,
    val birthDay: String,
    val age: String,
    val isEvolutionAvailable: Boolean,
    val nextFullnessUpdateTime: Long,
    val nextHealthUpdateTime: Long,
    val coin: Int
)

fun FlupetResponse.toMainUIModel(): MainUIModel {
    var standard = ""
    var nodding = ""
    var sleeping = ""
    var feelingGood = ""
    this.imageUrl.forEachIndexed { index: Int, s: String ->
        when (index) {
            0 -> standard = s
            1 -> nodding = s
            2 -> sleeping = s
            else -> feelingGood = s
        }
    }
    return MainUIModel(
        coin = this.coin,
        flupet = Flupet(
            this.fullness,
            this.health,
            ImageUrls(standard, nodding, sleeping, feelingGood),
            this.flupetName,
            this.birthDay,
            this.age,
            this.isEvolutionAvailable
        ),
        nextFullnessUpdateTime,
        nextHealthUpdateTime
    )
}
