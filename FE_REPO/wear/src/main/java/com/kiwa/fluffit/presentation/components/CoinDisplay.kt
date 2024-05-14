package com.kiwa.fluffit.presentation.components

import android.icu.text.DecimalFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.kiwa.fluffit.R
import com.kiwa.fluffit.presentation.theme.fluffitWearFontFamily

@Composable
fun CoinDisplay(coin: Long?, textColor: Color = Color.White) {
    val formattedCoin = DecimalFormat("#,###").format(coin ?: 0)

    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            modifier = Modifier.size(15.dp).padding(end = 3.dp),
            painter = painterResource(R.drawable.coin),
            contentDescription = "footprint"
        )
        Text(
            fontSize = 15.sp,
            text = "$formattedCoin",
            color = textColor,
        )
    }
}
