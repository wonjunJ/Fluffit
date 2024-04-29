package com.kiwa.fluffit.login

import androidx.lifecycle.viewModelScope
import com.kiwa.domain.usecase.CheckAccessTokenUseCase
import com.kiwa.domain.usecase.TryAutoLoginUseCase
import com.kiwa.fluffit.home.composebase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val checkAccessTokenUseCase: CheckAccessTokenUseCase,
    private val tryAutoLoginUseCase: TryAutoLoginUseCase
) : BaseViewModel<LoginViewState, LoginViewEvent>() {
    override fun createInitialState(): LoginViewState =
        LoginViewState.Splash()


    override fun onTriggerEvent(event: LoginViewEvent) {
        setEvent(event)
    }

    init {
        viewModelScope.launch {
            uiEvent.collect { event ->
                loginViewEvent(event)
            }
        }
    }

    private suspend fun loginViewEvent(event: LoginViewEvent) {
        when (event) {
            is LoginViewEvent.AttemptAutoLogin -> checkAccessToken()
            is LoginViewEvent.TryAutoLogin -> tryAutoLogin()
            LoginViewEvent.SuccessAutoLogin -> TODO()
            LoginViewEvent.FailedAutoLogin -> TODO()
        }
    }

    private suspend fun checkAccessToken() {
        checkAccessTokenUseCase().fold(
            onSuccess = {
                onTriggerEvent(LoginViewEvent.TryAutoLogin)
            },
            onFailure = {
                setState { LoginViewState.Default() }
            }
        )
    }

    private suspend fun tryAutoLogin(){
        tryAutoLoginUseCase().fold(
            onSuccess = { setState { LoginViewState.AutoLogin() }},
            onFailure = { setState { LoginViewState.Default() }}
        )
    }
}
