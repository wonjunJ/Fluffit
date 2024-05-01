package com.kiwa.fluffit.login

import androidx.lifecycle.viewModelScope
import com.kiwa.domain.usecase.CheckAccessTokenUseCase
import com.kiwa.domain.usecase.GetNaverIdUseCase
import com.kiwa.domain.usecase.SignInNaverUseCase
import com.kiwa.domain.usecase.TryAutoLoginUseCase
import com.kiwa.fluffit.home.composebase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val checkAccessTokenUseCase: CheckAccessTokenUseCase,
    private val tryAutoLoginUseCase: TryAutoLoginUseCase,
    private val getNaverIdUseCase: GetNaverIdUseCase,
    private val signInNaverUseCase: SignInNaverUseCase,
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
            LoginViewEvent.OnClickNaverLoginButton -> onAttemptNaverLogin()
            is LoginViewEvent.AttemptToFetchNaverId -> fetchUserNaverId(event.accessToken)
            is LoginViewEvent.ShowToast -> setState { showToast(event.message) }
            LoginViewEvent.OnFinishToast -> setState { onFinishToast() }
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

    private suspend fun fetchUserNaverId(accessToken: String) {
        getNaverIdUseCase(accessToken).fold(
            onSuccess = {
                tryToLogin(it)
            },
            onFailure = { setState { showToast(it.message ?: "네이버 접속 실패") } }
        )
    }

    private suspend fun tryToLogin(naverId: String) {
        signInNaverUseCase(naverId).fold(
            onSuccess = {
                setState { LoginViewState.Login() }
            },
            onFailure = {
                setState { showToast(it.message ?: "네트워크 에러") }
            }
        )
    }

    private suspend fun onAttemptNaverLogin() {
        return setState { LoginViewState.Default() }
    }

    private fun LoginViewState.showToast(message: String): LoginViewState {
        return when (this) {
            is LoginViewState.Default -> copy(toastMessage = message)
            is LoginViewState.Login -> copy(toastMessage = message)
            is LoginViewState.Splash -> copy(toastMessage = message)
            is LoginViewState.AutoLogin -> copy(toastMessage = message)
        }
    }

    private fun LoginViewState.onFinishToast(): LoginViewState {
        return when (this) {
            is LoginViewState.Default -> copy(toastMessage = "")
            is LoginViewState.Login -> copy(toastMessage = "")
            is LoginViewState.Splash -> copy(toastMessage = "")
            is LoginViewState.AutoLogin -> copy(toastMessage = "")
        }
    }
}
