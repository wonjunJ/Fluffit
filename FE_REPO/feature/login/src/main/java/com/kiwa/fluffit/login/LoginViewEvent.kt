package com.kiwa.fluffit.login

import com.kiwa.fluffit.base.ViewEvent

sealed class LoginViewEvent : ViewEvent {
    data object AttemptAutoLogin : LoginViewEvent()

    data object TryAutoLogin : LoginViewEvent()

    data object OnClickNaverLoginButton : LoginViewEvent()

    data object TryNicknameSetting : LoginViewEvent()

    data class AttempToModifyNickname(val name: String) : LoginViewEvent()

    data class AttemptToFetchNaverId(val accessToken: String) : LoginViewEvent()

    data class OnClickBackButton(val backPressedTime: Long) : LoginViewEvent()

    data object FinishProfileCheck : LoginViewEvent()

    data class ShowToast(val message: String) : LoginViewEvent()

    data object OnFinishToast : LoginViewEvent()
}
