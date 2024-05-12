package com.kiwa.fluffit.model.main.response

import com.kiwa.fluffit.model.flupet.FlupetStatus
import com.kiwa.fluffit.model.main.Flupet
import com.kiwa.fluffit.model.main.ImageUrls
import com.kiwa.fluffit.model.main.MainUIModel

data class NewEggResponse(
    val flupetName: String,
    val imageUrl: List<String>,
    val fullness: Int,
    val health: Int,
    val nextFullnessUpdateTime: Long,
    val nextHealthUpdateTime: Long
)

fun NewEggResponse.toMainUIModel(): MainUIModel {
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
        coin = 0,
        flupet = Flupet(
            this.fullness,
            this.health,
            ImageUrls(standard, nodding, sleeping, feelingGood),
            this.flupetName,
            "",
            "",
            false
        ),
        nextFullnessUpdateTime,
        nextHealthUpdateTime,
        FlupetStatus.Alive
    )
}
