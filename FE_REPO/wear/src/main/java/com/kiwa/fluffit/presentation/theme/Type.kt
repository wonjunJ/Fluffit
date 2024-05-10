package com.kiwa.fluffit.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Typography
import com.kiwa.fluffit.R

val fluffitWearFontFamily = FontFamily(
    Font(R.font.neodgm, FontWeight.Normal, FontStyle.Normal)
)

val fluffitTypography = Typography(
    title1 = TextStyle(
        fontFamily = fluffitWearFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 48.sp,
        lineHeight = 48.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = Color.White
    ),

    title2 = TextStyle(
        fontFamily = fluffitWearFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 40.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = Color.White
    ),

    title3 = TextStyle(
        fontFamily = fluffitWearFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = Color.White
    ),

    body1 = TextStyle(
        fontFamily = fluffitWearFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = Color.White
    ),

    body2 = TextStyle(
        fontFamily = fluffitWearFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = Color.White
    ),

    button = TextStyle(
        fontFamily = fluffitWearFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = Color.Black
    ),

    display1 = TextStyle(
        fontFamily = fluffitWearFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = Color.White
    ),

    display2 = TextStyle(
        fontFamily = fluffitWearFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = Color.White
    ),

    display3 = TextStyle(
        fontFamily = fluffitWearFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = Color.White
    ),

    caption3 = TextStyle(
        fontFamily = fluffitWearFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        color = Color.White
    ),
)
