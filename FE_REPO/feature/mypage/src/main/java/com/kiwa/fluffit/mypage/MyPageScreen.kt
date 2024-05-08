package com.kiwa.fluffit.mypage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun MyPageScreen(
    viewModel: MyPageViewModel = hiltViewModel<MyPageViewModel>()
){
    val viewState = viewModel.uiState.collectAsState().value


}
