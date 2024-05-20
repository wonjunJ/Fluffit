package com.kiwa.fluffit.mypage

import com.kiwa.fluffit.base.ViewState

sealed class MyPageViewState : ViewState {
    abstract val toastMessage: String
    abstract val isLogin: Boolean
    abstract val isTryingSignOut: Boolean
    abstract val isLoadingUserName: Boolean
    abstract val isEditUserName: Boolean
    abstract val name: String

    data class Init(
        override val toastMessage: String = "",
        override val isLogin: Boolean = true,
        override val isTryingSignOut: Boolean = false,
        override val isLoadingUserName: Boolean = true,
        override val isEditUserName: Boolean = false,
        override val name: String = ""
    ) : MyPageViewState()

    data class Default(
        override val toastMessage: String = "",
        override val isLogin: Boolean = true,
        override val isTryingSignOut: Boolean = false,
        override val isLoadingUserName: Boolean = false,
        override val isEditUserName: Boolean = false,
        override val name: String
    ) : MyPageViewState()

    data class EditName(
        override val toastMessage: String = "",
        override val isLogin: Boolean = true,
        override val isTryingSignOut: Boolean = false,
        override val isLoadingUserName: Boolean = false,
        override val isEditUserName: Boolean = true,
        override val name: String
    ) : MyPageViewState()
}
