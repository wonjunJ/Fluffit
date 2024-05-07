package com.kiwa.fluffit.home.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kiwa.fluffit.home.R

@Composable
private fun StatDisplay(@DrawableRes statIconImage: Int, @DrawableRes statProgressImage: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = statIconImage),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Image(
            painter = painterResource(id = statProgressImage),
            contentDescription = null,
            modifier = Modifier.padding(start = 8.dp).height(32.dp)
        )
    }
}

@Composable
internal fun FullnessDisplay(stat: Int) {
    val image = when (stat) {
        0 -> R.drawable.fullness_0
        in 1..20 -> R.drawable.fullness_20
        in 21..40 -> R.drawable.fullness_40
        in 41..60 -> R.drawable.fullness_60
        in 61..80 -> R.drawable.fullness_80
        else -> R.drawable.fullness_100
    }
    StatDisplay(statIconImage = R.drawable.fullness, statProgressImage = image)
}

@Composable
internal fun HealthDisplay(stat: Int) {
    val image = when (stat) {
        0 -> R.drawable.health_0
        in 1..20 -> R.drawable.health_20
        in 21..40 -> R.drawable.health_40
        in 41..60 -> R.drawable.health_60
        in 61..80 -> R.drawable.health_80
        else -> R.drawable.health_100
    }
    StatDisplay(statIconImage = R.drawable.health, statProgressImage = image)
}
