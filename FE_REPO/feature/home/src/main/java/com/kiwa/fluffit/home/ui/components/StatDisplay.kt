package com.kiwa.fluffit.home.ui.components

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

@Composable
internal fun StatDisplay(@DrawableRes statIconImage: Int, @DrawableRes statProgressImage: Int) {
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
