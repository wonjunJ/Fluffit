package com.kiwa.fluffit.presentation.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme

@Composable
fun FluffitTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        typography = fluffitTypography,
        content = content
    )
}
