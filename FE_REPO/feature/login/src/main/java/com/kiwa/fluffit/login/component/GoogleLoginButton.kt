package com.kiwa.fluffit.login.component

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
import com.kiwa.fluffit.login.R

@Composable
internal fun GoogleLoginButton(
    onNavigationToHome: () -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Image(
            modifier = Modifier
                .padding(bottom = 90.dp)
                .align(Alignment.BottomCenter)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { onNavigationToHome },
            painter = painterResource(id = R.drawable.continue_with_google),
            contentDescription = "구글 로그인",
        )
    }
}