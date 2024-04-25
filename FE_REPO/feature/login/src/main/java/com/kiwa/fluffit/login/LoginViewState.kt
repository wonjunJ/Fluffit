package com.kiwa.fluffit.login

import com.kiwa.fluffit.home.composebase.ViewState

sealed class LoginViewState : ViewState{
    abstract val isTryingAutoLogin: Boolean

    data class Splash(
        override val isTryingAutoLogin: Boolean = true
    ) : LoginViewState()

    data class Default(
        override val isTryingAutoLogin: Boolean = false
    ) : LoginViewState()

}