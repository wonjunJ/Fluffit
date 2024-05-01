package com.kiwa.fluffit.login

import com.kiwa.fluffit.home.composebase.ViewState

sealed class LoginViewState : ViewState{
    abstract val isTryingAutoLogin: Boolean
    abstract val shouldDoLogin: Boolean
    abstract val toastMessage: String

    //초기 자동 로그인을 위한 accesstoken 보유 확인 상태
    data class Splash(
        override val isTryingAutoLogin: Boolean = true,
        override val shouldDoLogin: Boolean = true,
        override val toastMessage: String = "",
    ) : LoginViewState()

    //auto login 시도 상태
    data class AutoLogin(
        override val isTryingAutoLogin: Boolean = true,
        override val shouldDoLogin: Boolean = false,
        override val toastMessage: String = "",
    ) : LoginViewState()

    //auto login 실패 후 로그인 버튼 등장 상태
    data class Default(
        override val isTryingAutoLogin: Boolean = false,
        override val shouldDoLogin: Boolean = true,
        override val toastMessage: String = "",
    ) : LoginViewState()

    //로그인 버튼 클릭 후 상태
    data class Login(
        override val isTryingAutoLogin: Boolean = false,
        override val shouldDoLogin: Boolean = false,
        override val toastMessage: String = "",
    ) : LoginViewState()

}
