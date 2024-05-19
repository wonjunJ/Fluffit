package com.kiwa.fluffit.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiwa.fluffit.designsystem.theme.fluffitTypography

@Composable
internal fun MyPageView(
    viewState: MyPageViewState,
    viewModel: MyPageViewModel,
    clickFlupetHistory: () -> Unit,
    clickBattleRecord: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth().alpha(0.6f),
            painter = painterResource(id = R.drawable.mypagebackground),
            contentDescription = "배경화면",
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 32.dp)
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "마이페이지",
                style = fluffitTypography.bodyLarge
            )
            Spacer(modifier = Modifier.height(30.dp))
            UserNameUI(
                viewState = viewState,
                onClickPencilButton = { viewModel.onTriggerEvent(MyPageViewEvent.OnClickPencil) },
                onClickConfirmButton = { name ->
                    viewModel.onTriggerEvent(MyPageViewEvent.OnClickModifyUserName(name))
                },
                name = viewState.name
            )
            MyPageOutLinedText(
                onClickText = { clickFlupetHistory() },
                text = "플러펫 히스토리",
                textStyle = fluffitTypography.bodyLarge.merge(
                    TextStyle(
                        fontSize = 24.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
            )
            MyPageOutLinedText(
                onClickText = { clickBattleRecord() },
                text = "배틀 전적",
                textStyle = fluffitTypography.bodyLarge.merge(
                    TextStyle(
                        fontSize = 24.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
            )
            MyPageOutLinedText(
                onClickText = { },
                text = "개인정보 처리 방침 및\n서비스 이용약관",
                textStyle = fluffitTypography.bodyLarge.merge(
                    TextStyle(
                        textAlign = TextAlign.Start,
                        fontSize = 24.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
            )
            MyPageOutLinedText(
                onClickText = { viewModel.onTriggerEvent(MyPageViewEvent.OnClickLogout) },
                text = "로그아웃",
                textStyle = fluffitTypography.bodyLarge.merge(
                    TextStyle(
                        fontSize = 24.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
            )
            MyPageOutLinedText(
                onClickText = {
                    viewModel.onTriggerEvent(
                        MyPageViewEvent.OnClickSignOut
                    )
                },
                text = "회원탈퇴",
                textStyle = fluffitTypography.bodySmall.merge(
                    TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
            )
        }
    }
}

@Composable
internal fun MyPageOutLinedText(
    modifier: Modifier = Modifier.padding(vertical = 24.dp),
    onClickText: () -> Unit,
    text: String,
    textStyle: TextStyle,
    strokeColor: Color = Color.White,
    strokeWidth: Float = 1.5f
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
            modifier = Modifier.align(Alignment.Center)
        )
        Text(
            text = text,
            style = textStyle.merge(
                TextStyle(
                    color = strokeColor,
                    drawStyle = Stroke(width = strokeWidth, join = StrokeJoin.Round)
                )
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
