package com.kiwa.fluffit.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.wear.compose.material.MaterialTheme
import com.kiwa.fluffit.R

val fluffitWearFontFamily = FontFamily(
    Font(R.font.neodgm, FontWeight.Normal, FontStyle.Normal)
)

@Composable
fun FluffitTheme(
    content: @Composable () -> Unit
) {
    /**
     * Empty theme to customize for your app.
     * See: https://developer.android.com/jetpack/compose/designsystems/custom
     */
    MaterialTheme(
        content = content
    )
}