package com.kiwa.fluffit.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.kiwa.fluffit.designsystem.R

val fluffitMainFontFamily = FontFamily(
    Font(R.font.neodgm, FontWeight.Normal, FontStyle.Normal)
)

val fluffitTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = fluffitMainFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 48.sp,
        lineHeight = 48.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = Color.Black
    ),

    titleMedium = TextStyle(
        fontFamily = fluffitMainFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 40.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = Color.Black
    ),

    titleSmall = TextStyle(
        fontFamily = fluffitMainFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = Color.Black
    ),

    bodyLarge = TextStyle(
        fontFamily = fluffitMainFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = Color.Black
    ),

    bodyMedium = TextStyle(
        fontFamily = fluffitMainFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = Color.Black
    ),

    bodySmall = TextStyle(
        fontFamily = fluffitMainFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = Color.Black
    ),

    headlineLarge = TextStyle(
        fontFamily = fluffitMainFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = Color.Black
    ),

    headlineMedium = TextStyle(
        fontFamily = fluffitMainFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = Color.Black
    ),

    headlineSmall = TextStyle(
        fontFamily = fluffitMainFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = Color.Black
    )
)
