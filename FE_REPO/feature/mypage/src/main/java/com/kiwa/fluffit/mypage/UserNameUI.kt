package com.kiwa.fluffit.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiwa.fluffit.designsystem.theme.fluffitTypography

@Composable
fun UserNameUI(
    viewState: MyPageViewState,
    onClickPencilButton: () -> Unit,
    onClickConfirmButton: (String) -> Unit,
    name: String
) {
    Row(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 24.dp)
            .wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (viewState) {
            is MyPageViewState.EditName -> EditModeUI(name) {
                onClickConfirmButton(it)
            }

            else -> DisplayModeUI(name = name) {
                onClickPencilButton()
            }
        }
    }
}

@Composable
fun DisplayModeUI(name: String, onClickPencilButton: () -> Unit) {
    UserNameOutlinedText(
        onClickText = {},
        text = name,
        textStyle = fluffitTypography.bodySmall.merge(
            TextStyle(
                fontSize = 24.sp,
                color = Color.Black, fontWeight = FontWeight.Bold
            )
        ),
        modifier = Modifier.wrapContentWidth(),
        maxLines = 1
    )
    Image(
        painter = painterResource(id = R.drawable.pencil),
        contentDescription = null,
        modifier = Modifier
            .size(32.dp)
            .padding(start = 4.dp)
            .clickable {
                onClickPencilButton()
            }
    )
}

@Composable
fun EditModeUI(name: String, onClickConfirmButton: (String) -> Unit) {
    val textState = remember { mutableStateOf(name) }
    CustomTextField(
        textState = textState,
        isSingleLine = true,
        maxLength = 8,
        modifier = Modifier.wrapContentWidth()
    )

    Image(
        painter = painterResource(id = R.drawable.check),
        contentDescription = null,
        modifier = Modifier
            .size(32.dp)
            .padding(start = 4.dp)
            .clickable {
                onClickConfirmButton(textState.value)
            }
    )
}

@Composable
private fun UserNameOutlinedText(
    modifier: Modifier = Modifier.padding(vertical = 24.dp),
    onClickText: () -> Unit,
    text: String,
    textStyle: TextStyle,
    strokeColor: Color = Color.White,
    strokeWidth: Float = 1.5f,
    maxLines: Int = 1
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier.then(
            Modifier.clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClickText() }
        )
    ) {
        Text(
            text = text,
            style = textStyle,
            modifier = Modifier.align(Alignment.Center),
            maxLines = maxLines
        )
        Text(
            text = text,
            style = textStyle.merge(
                TextStyle(
                    color = strokeColor,
                    drawStyle = Stroke(width = strokeWidth, join = StrokeJoin.Round)
                )
            ),
            modifier = Modifier.align(Alignment.Center),
            maxLines = maxLines
        )
    }
}
