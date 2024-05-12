package com.kiwa.fluffit.login

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kiwa.fluffit.login.component.FluffitSnackBarHost
import com.kiwa.fluffit.login.component.NaverLoginButton
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

private const val TAG = "LoginScreen_싸피"

@Composable
internal fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel<LoginViewModel>(),
    onNavigationToHome: () -> Unit
) {
    val viewState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current
    val glide = remember { Glide.with(context) }
    val snackBarHostState = remember { SnackbarHostState() }

    if (viewState.isTryingAutoLogin) {
        viewModel.onTriggerEvent(LoginViewEvent.AttemptAutoLogin)
    }

    if(viewState.isNewUser){
        UserNicknameDialog(initialText = "닉네임입력") {

        }
    }
    
    LaunchedEffect(key1 = viewState.userName) {
        
    }

    ObserveLoginAttempt(viewState, context, viewModel)

    ObserveToastMessage(viewState, snackBarHostState, viewModel)

    LaunchedEffect(key1 = viewState.shouldExit) {
        if (viewState.shouldExit) {
            (context as Activity).finish()
        }
    }

    BackHandler {
        val backPressedTime = System.currentTimeMillis()
        Log.d(TAG, "LoginScreen: backPressedTime : $backPressedTime")
        viewModel.onTriggerEvent(LoginViewEvent.OnClickBackButton(backPressedTime))
    }

    ObserveNavigate(onNavigationToHome, viewState)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.login_background),
            contentDescription = "배경화면",
            contentScale = ContentScale.FillBounds
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
            enter = slideInVertically(initialOffsetY = {
                it
            }, animationSpec = tween(durationMillis = 3000)),
            exit = slideOutVertically(targetOffsetY = {
                it
            }, animationSpec = tween(durationMillis = 300))
        ) {
            NaverLoginButton(viewModel)
        }

        FluffitSnackBarHost(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
            snackBarHostState = snackBarHostState
        )
    }
}

@Composable
private fun ObserveLoginAttempt(
    viewState: LoginViewState,
    context: Context,
    viewModel: LoginViewModel
) {
    LaunchedEffect(key1 = viewState.clickLoginButton) {
        if (viewState.clickLoginButton) {
            val result = authenticateWithNaver(context = context)
            result.fold(
                onSuccess = {
                    viewModel.onTriggerEvent(LoginViewEvent.AttemptToFetchNaverId(it))
                },
                onFailure = { _ ->
                    Log.d(TAG, "ObserveLoginAttempt: 실패")
                }
            )
        }
    }
}

@Composable
private fun ObserveToastMessage(
    viewState: LoginViewState,
    snackBarHostState: SnackbarHostState,
    viewModel: LoginViewModel
) {
    LaunchedEffect(key1 = viewState.toastMessage) {
        Log.d(TAG, "ObserveToastMessage: ${viewState.toastMessage}")
        if (viewState.toastMessage.isNotEmpty()) {
            snackBarHostState.currentSnackbarData?.dismiss()
            snackBarHostState.showSnackbar(
                viewState.toastMessage,
                actionLabel = "확인",
                duration = SnackbarDuration.Short
            )
            viewModel.onTriggerEvent(LoginViewEvent.OnFinishToast)
        }
    }
}

@Composable
private fun ObserveNavigate(
    onNavigationToHome: () -> Unit,
    viewState: LoginViewState
) {
    if (viewState.navigateToHome) {
        Log.d(TAG, "ObserveNavigate: 이동합니다 ${viewState.navigateToHome}")
        onNavigationToHome()
    }
}

private suspend fun authenticateWithNaver(context: Context): Result<String> =
    suspendCancellableCoroutine { continuation ->
        val callback = object : OAuthLoginCallback {
            override fun onSuccess() {
                val accessToken = NaverIdLoginSDK.getAccessToken() ?: ""
                continuation.resume(Result.success(accessToken))
            }

            override fun onFailure(httpStatus: Int, message: String) {
                continuation.resume(
                    Result.failure(
                        Exception("Naver login failed: $httpStatus, $message")
                    )
                )
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(context, callback)

        continuation.invokeOnCancellation {}
    }
