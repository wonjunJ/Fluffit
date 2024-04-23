package com.kiwa.fluffit.login.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.kiwa.fluffit.login.R

// Set of Material typography styles to start with

val fluffitMainFontFamily = FontFamily(
    Font(R.font.neodgm, FontWeight.Normal, FontStyle.Normal)
)

val fluffitTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = fluffitMainFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp,
        lineHeight = 48.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = smooth_black
    ),

    titleMedium = TextStyle(
        fontFamily = fluffitMainFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = smooth_black
    ),

    titleSmall = TextStyle(
        fontFamily = fluffitMainFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = smooth_black
    ),

    bodyLarge = TextStyle(
        fontFamily = fluffitMainFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = smooth_black
    ),

    bodyMedium = TextStyle(
        fontFamily = fluffitMainFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = smooth_black
    ),

    bodySmall = TextStyle(
        fontFamily = fluffitMainFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = smooth_black
    ),
)