package com.kiwa.fluffit.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.kiwa.domain.TokenManager
import com.kiwa.domain.usecase.CheckAccessTokenUseCase
import com.kiwa.domain.usecase.CheckUserProfileUseCase
import com.kiwa.domain.usecase.GetNaverIdUseCase
import com.kiwa.domain.usecase.SignInNaverUseCase
import com.kiwa.domain.usecase.TryAutoLoginUseCase
import com.kiwa.fluffit.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoginViewModel_싸피"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val checkAccessTokenUseCase: CheckAccessTokenUseCase,
    private val tryAutoLoginUseCase: TryAutoLoginUseCase,
    private val getNaverIdUseCase: GetNaverIdUseCase,
    private val signInNaverUseCase: SignInNaverUseCase,
    private val checkUserProfileUseCase: CheckUserProfileUseCase,
    private val tokenManager: TokenManager
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
            is LoginViewEvent.OnClickBackButton -> setState {
                onClickBackButton(
                    event.backPressedTime
                )
            }
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

    private suspend fun tryAutoLogin() {
        tryAutoLoginUseCase().fold(
            onSuccess = {
                setState { LoginViewState.LoginSuccess() }
            },
            onFailure = {
                setState { LoginViewState.Default() }
            }
        )
    }

    private suspend fun fetchUserNaverId(accessToken: String) {
        getNaverIdUseCase(accessToken).fold(
            onSuccess = {
                Log.d(TAG, "fetchUserNaverId: $it")
                tryToLogin(it)
            },
            onFailure = {
                setState { showToast(it.message ?: "네이버 접속 실패") }
            }
        )
    }

    private suspend fun tryToLogin(naverId: String) {
        signInNaverUseCase(naverId).fold(
            onSuccess = {
                checkUserProfile()
            },
            onFailure = {
                setState { showToast(it.message ?: "네트워크 에러") }
                setState { LoginViewState.Default() }
            }
        )
    }

    private suspend fun checkUserProfile() {
        val accessToken = tokenManager.getAccessToken()
        checkUserProfileUseCase(accessToken).fold(
            onSuccess = {
                Log.d(TAG, "checkUserProfile: 성공")
                setState { LoginViewState.LoginSuccess() }
            },
            onFailure = {
                Log.d(TAG, "checkUserProfile: 실패")
                setState { LoginViewState.SignIn() }
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
            is LoginViewState.SignIn -> copy(toastMessage = message)
            is LoginViewState.LoginSuccess -> copy(toastMessage = message)
        }
    }

    private fun LoginViewState.onFinishToast(): LoginViewState {
        return when (this) {
            is LoginViewState.Default -> copy(toastMessage = "")
            is LoginViewState.Login -> copy(toastMessage = "")
            is LoginViewState.Splash -> copy(toastMessage = "")
            is LoginViewState.AutoLogin -> copy(toastMessage = "")
            is LoginViewState.SignIn -> copy(toastMessage = "")
            is LoginViewState.LoginSuccess -> copy(toastMessage = "")
        }
    }

    private fun LoginViewState.onClickBackButton(backPressedTime: Long): LoginViewState {
        if (checkBackPressedRightBefore(backPressedTime, currentState.lastBackPressedTime)) {
            return when (this) {
                is LoginViewState.Default -> copy(shouldExit = true)
                is LoginViewState.Login -> copy(shouldExit = true)
                is LoginViewState.Splash -> copy(shouldExit = true)
                is LoginViewState.AutoLogin -> copy(shouldExit = true)
                is LoginViewState.SignIn -> copy(shouldExit = true)
                is LoginViewState.LoginSuccess -> copy(shouldExit = true)
            }
        }

        return when (this) {
            is LoginViewState.Default -> copy(
                lastBackPressedTime = backPressedTime,
                toastMessage = "한번 더 누르면 종료됩니다."
            )

            is LoginViewState.Login -> copy(
                lastBackPressedTime = backPressedTime,
                toastMessage = "한번 더 누르면 종료됩니다."
            )

            is LoginViewState.Splash -> copy(
                lastBackPressedTime = backPressedTime,
                toastMessage = "한번 더 누르면 종료됩니다."
            )

            is LoginViewState.AutoLogin -> copy(
                lastBackPressedTime = backPressedTime,
                toastMessage = "한번 더 누르면 종료됩니다."
            )

            is LoginViewState.SignIn -> copy(
                lastBackPressedTime = backPressedTime,
                toastMessage = "한번 더 누르면 종료됩니다."
            )

            is LoginViewState.LoginSuccess -> copy(
                lastBackPressedTime = backPressedTime,
                toastMessage = "한번 더 누르면 종료됩니다."
            )
        }
    }

    private fun checkBackPressedRightBefore(
        backPressedTime: Long,
        lastBackPressedTime: Long
    ): Boolean {
        return backPressedTime - lastBackPressedTime < 1000
    }
}
