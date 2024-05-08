package com.kiwa.fluffit.mypage

import com.kiwa.fluffit.base.ViewState

sealed class MyPageViewState : ViewState {
    abstract val toastMessage: String

    data class Init(
        override val toastMessage: String = ""
    ) : MyPageViewState()


}
