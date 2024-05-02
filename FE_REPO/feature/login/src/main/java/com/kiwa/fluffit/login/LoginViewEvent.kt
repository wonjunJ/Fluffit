package com.kiwa.fluffit.login

import com.kiwa.fluffit.home.composebase.ViewEvent

sealed class LoginViewEvent : ViewEvent {
    data object AttemptAutoLogin : LoginViewEvent()

    data object TryAutoLogin : LoginViewEvent()

    data object OnClickNaverLoginButton : LoginViewEvent()

    data class AttemptToFetchNaverId(val accessToken: String) : LoginViewEvent()

    data class ShowToast(val message: String) : LoginViewEvent()

    data object OnFinishToast : LoginViewEvent()
}
