package com.kiwa.fluffit.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.kiwa.domain.usecase.CheckAccessTokenUseCase
import com.kiwa.domain.usecase.GetNaverIdUseCase
import com.kiwa.domain.usecase.SignInNaverUseCase
import com.kiwa.domain.usecase.TryAutoLoginUseCase
import com.kiwa.fluffit.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoginViewModel 싸피"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val checkAccessTokenUseCase: CheckAccessTokenUseCase,
    private val tryAutoLoginUseCase: TryAutoLoginUseCase,
    private val getNaverIdUseCase: GetNaverIdUseCase,
    private val signInNaverUseCase: SignInNaverUseCase
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
                Log.d(TAG, "checkAccessToken: AccessToken 확인 성공")
                onTriggerEvent(LoginViewEvent.TryAutoLogin)
            },
            onFailure = {
                Log.d(TAG, "checkAccessToken: AccessToken 확인 실패")
                setState { LoginViewState.Default() }
            }
        )
    }

    private suspend fun tryAutoLogin() {
        tryAutoLoginUseCase().fold(
            onSuccess = {
                Log.d(TAG, "tryAutoLogin: AutoLogin 성공")
                setState { LoginViewState.AutoLogin() }
            },
            onFailure = {
                Log.d(TAG, "tryAutoLogin: AutoLogin 실패")
                setState { LoginViewState.Default() }
            }
        )
    }

    private suspend fun fetchUserNaverId(accessToken: String) {
        getNaverIdUseCase(accessToken).fold(
            onSuccess = {
                Log.d(TAG, "fetchUserNaverId: 네이버 ID 가져오기 성공")
                tryToLogin(it)
            },
            onFailure = {
                Log.d(TAG, "fetchUserNaverId: 네이버 ID 가져오기 실패")
                setState { showToast(it.message ?: "네이버 접속 실패") }
            }
        )
    }

    private suspend fun tryToLogin(naverId: String) {
        signInNaverUseCase(naverId).fold(
            onSuccess = {
                Log.d(TAG, "tryToLogin: 네이버ID 값 : $naverId")
                Log.d(TAG, "tryToLogin: 로그인 시도 성공")
                setState { LoginViewState.LoginSuccess() }
            },
            onFailure = {
                Log.d(TAG, "tryToLogin: 로그인 시도 실패")
                setState { showToast(it.message ?: "네트워크 에러") }
                setState { LoginViewState.Default() }
            }
        )
    }

    private fun onAttemptNaverLogin() {
        return setState { LoginViewState.Login() }
    }

    private fun LoginViewState.showToast(message: String): LoginViewState {
        return when (this) {
            is LoginViewState.Default -> copy(toastMessage = message)
            is LoginViewState.Login -> copy(toastMessage = message)
            is LoginViewState.Splash -> copy(toastMessage = message)
            is LoginViewState.AutoLogin -> copy(toastMessage = message)
            is LoginViewState.LoginSuccess -> copy(toastMessage = message)
        }
    }

    private fun LoginViewState.onFinishToast(): LoginViewState {
        return when (this) {
            is LoginViewState.Default -> copy(toastMessage = "")
            is LoginViewState.Login -> copy(toastMessage = "")
            is LoginViewState.Splash -> copy(toastMessage = "")
            is LoginViewState.AutoLogin -> copy(toastMessage = "")
            is LoginViewState.LoginSuccess -> copy(toastMessage = "")
        }
    }
}
