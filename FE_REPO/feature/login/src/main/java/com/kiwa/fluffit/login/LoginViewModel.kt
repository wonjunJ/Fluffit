package com.kiwa.fluffit.login

import androidx.lifecycle.viewModelScope
import com.kiwa.fluffit.home.composebase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

) : BaseViewModel<LoginViewState, LoginViewEvent>() {
    override fun createInitialState(): LoginViewState =
        LoginViewState.Splash()


    override fun onTriggerEvent(event: LoginViewEvent) {
        setEvent(event)
    }

    init {
        viewModelScope.launch {
            uiEvent.collect{event ->
                loginViewEvent(event)
            }
        }
    }

    private suspend fun loginViewEvent(event:LoginViewEvent){

    }
}