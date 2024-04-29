package com.kiwa.fluffit.login

import com.kiwa.fluffit.home.composebase.ViewEvent

sealed class LoginViewEvent :ViewEvent{
    data object AttemptAutoLogin : LoginViewEvent()

    data object SuccessAutoLogin : LoginViewEvent()

    data object FailedAutoLogin : LoginViewEvent()

}