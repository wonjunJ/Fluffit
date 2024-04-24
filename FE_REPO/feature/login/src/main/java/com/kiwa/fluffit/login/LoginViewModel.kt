package com.kiwa.fluffit.login

import com.kiwa.fluffit.home.composebase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

) : BaseViewModel<LoginViewState, LoginViewEvent>() {
    override fun createInitialState(): LoginViewState =
        LoginViewState.Splash()


    override fun onTriggerEvent(event: LoginViewEvent) {
        setEvent(event)
    }

}