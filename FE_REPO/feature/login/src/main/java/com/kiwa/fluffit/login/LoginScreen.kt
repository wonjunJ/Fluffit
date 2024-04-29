package com.kiwa.fluffit.login

import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kiwa.fluffit.login.component.NaverLoginButton

@Composable
internal fun LoginScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: LoginViewModel = hiltViewModel<LoginViewModel>(),
    onNavigationToHome: () -> Unit
) {
    val viewState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current
    val glide = remember { Glide.with(context) }
    var visible by remember { mutableStateOf(false) }

    if (viewState.isTryingAutoLogin) {
        viewModel.onTriggerEvent(LoginViewEvent.AttemptAutoLogin)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.login_background),
            contentDescription = "배경화면",
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .padding(bottom = 180.dp)
                .fillMaxSize(0.45f)
                .align(Alignment.BottomCenter)
        ) {
            AndroidView(
                factory = { ctx ->
                    ImageView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                update = { imageView ->
                    glide
                        .asGif()
                        .load(com.kiwa.fluffit.home.R.raw.rabbit_white)
                        .apply(RequestOptions().fitCenter())
                        .into(imageView)
                        .clearOnDetach()
                }
            )
        }

        AnimatedVisibility(
            visible = !viewState.isTryingAutoLogin,
            modifier = Modifier.align(Alignment.Center),
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
            exit = fadeOut(animationSpec = tween(durationMillis = 1000))
//            enter = slideInVertically(initialOffsetY = {
//                it
//            }, animationSpec = tween(durationMillis = 1000)),
//            exit = slideOutVertically(targetOffsetY = {
//                it
//            }, animationSpec = tween(durationMillis = 1000))
        ) {
            NaverLoginButton(onNavigationToHome)
        }
    }
}
