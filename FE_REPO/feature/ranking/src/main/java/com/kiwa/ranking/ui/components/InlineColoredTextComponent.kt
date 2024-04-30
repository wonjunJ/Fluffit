package com.kiwa.ranking.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun InlineColoredTextComponent(
    modifier: Modifier,
    inputText: String,
    inlineTextColor: Color = Color.White,
    fontSize: TextUnit,
    fontFamily: androidx.compose.ui.text.font.FontFamily
) {
    Box(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = inputText,
            color = inlineTextColor,
            fontFamily = fontFamily,
            fontSize = fontSize,
            letterSpacing = (-6).sp
        )

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = inputText,
            color = Color.Black,
            fontFamily = fontFamily,
            fontSize = fontSize,
            letterSpacing = (-6).sp,
            style = TextStyle.Default.copy(
                fontSize = fontSize,
                drawStyle = Stroke(
                    miter = 2f,
                    width = 20f
                )
            )
        )
    }
}
