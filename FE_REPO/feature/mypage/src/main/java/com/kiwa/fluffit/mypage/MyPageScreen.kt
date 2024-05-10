package com.kiwa.fluffit.mypage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.navercorp.nid.NaverIdLoginSDK

@Composable
internal fun MyPageScreen(
    viewModel: MyPageViewModel = hiltViewModel<MyPageViewModel>(),
    onClickFlupetHistory: () -> Unit,
    onClickBattleRecord: () -> Unit,
    onClickLogout: () -> Unit
) {
    val viewState = viewModel.uiState.collectAsState().value
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewState.isLogin) {
        if (!viewState.isLogin) onClickLogout()
    }

    LaunchedEffect(key1 = viewState.name) {
        viewModel.onTriggerEvent(MyPageViewEvent.SetChangedUserName(viewState.name))
    }

    if (viewState.isLoadingUserName) {
        viewModel.onTriggerEvent(MyPageViewEvent.Initialize)
    }

    if (viewState.isTryingSignOut) {
        SignOutDialog(
            { viewModel.onTriggerEvent(MyPageViewEvent.OnClickSignOutConfirm(fetchNaverAccessToken())) },
            { viewModel.onTriggerEvent(MyPageViewEvent.OnCancelSignOut) }
        )
    }

    if (viewState is MyPageViewState.Init) {
        viewModel.onTriggerEvent(MyPageViewEvent.Initialize)
    }

    ObserveToastMessage(viewState, snackBarHostState, viewModel)

    Box {
        //ui 확인용 코드
        if (viewState is MyPageViewState.Default || viewState is MyPageViewState.Init || viewState is MyPageViewState.EditName) {
            MyPageView(viewState, viewModel, onClickFlupetHistory, onClickBattleRecord)
        }

        //실제 코드
//        if (viewState is MyPageViewState.Default) {
//            MyPageView(viewState, viewModel, onClickFlupetHistory, onClickBattleRecord)
//        }

        FluffitSnackBarHost(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
            snackBarHostState = snackBarHostState
        )
    }
}

private fun fetchNaverAccessToken(): String =
    NaverIdLoginSDK.getAccessToken() ?: ""

@Composable
private fun ObserveToastMessage(
    viewState: MyPageViewState,
    snackBarHostState: SnackbarHostState,
    viewModel: MyPageViewModel
) {
    LaunchedEffect(key1 = viewState.toastMessage) {
        if (viewState.toastMessage.isNotEmpty()) {
            snackBarHostState.currentSnackbarData?.dismiss()
            snackBarHostState.showSnackbar(
                viewState.toastMessage,
                actionLabel = "확인",
                duration = SnackbarDuration.Short
            )
            viewModel.onTriggerEvent(MyPageViewEvent.OnFinishToast)
        }
    }
}
