package com.kiwa.fluffit.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kiwa.fluffit.designsystem.theme.fluffitTypography

@Composable
internal fun SignOutDialog(onClickConfirmButton: () -> Unit, onClickCancelButton: () -> Unit) {
    Dialog(onDismissRequest = { onClickCancelButton() }) {
        Box(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .clip(shape = RoundedCornerShape(32.dp))
                .wrapContentHeight()
                .background(Color.White)
                .padding(top = 24.dp, bottom = 12.dp)

        ) {
            Image(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .align(Alignment.CenterEnd)
                    .alpha(0.5f),
                painter = painterResource(id = R.drawable.rabbit_white),
                contentDescription = null
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center)
            ) {
                SignOutDialogText("정말 탈퇴하실건가요?\n플러펫과의 추억이\n모두 사라지게 됩니다!") {}

                Spacer(modifier = Modifier.height(24.dp))

                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    SignOutDialogText("취소") { onClickCancelButton() }
                    Spacer(modifier = Modifier.weight(1f))
                    SignOutDialogText("확인") { onClickConfirmButton() }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun SignOutDialogText(text: String, onClickText: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier.clickable(
            interactionSource = interactionSource,
            indication = null
        ) { onClickText() }
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            textAlign = TextAlign.Center,
            style = fluffitTypography.bodyMedium
        )
    }
}
