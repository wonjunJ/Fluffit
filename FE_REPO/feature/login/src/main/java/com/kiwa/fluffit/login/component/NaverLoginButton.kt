package com.kiwa.fluffit.login.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kiwa.fluffit.login.LoginViewEvent
import com.kiwa.fluffit.login.LoginViewModel
import com.kiwa.fluffit.login.R

private const val TAG = "NaverLoginButton_싸피"

@Composable
internal fun NaverLoginButton(
    viewModel: LoginViewModel
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .padding(bottom = 90.dp)
                .align(Alignment.BottomCenter)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    viewModel.onTriggerEvent(LoginViewEvent.OnClickNaverLoginButton)
                    Log.d(TAG, "NaverLoginButton: 클릭된듯")
                },
            painter = painterResource(id = R.drawable.naver_login_white),
            contentDescription = "네이버 로그인",
        )
    }
}
