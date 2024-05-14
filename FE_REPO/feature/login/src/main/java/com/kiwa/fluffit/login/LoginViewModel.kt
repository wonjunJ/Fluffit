package com.kiwa.fluffit.login

import androidx.lifecycle.viewModelScope
import com.kiwa.domain.TokenManager
import com.kiwa.domain.usecase.CheckAccessTokenUseCase
import com.kiwa.domain.usecase.CheckUserProfileUseCase
import com.kiwa.domain.usecase.GetNaverIdUseCase
import com.kiwa.domain.usecase.SignInNaverUseCase
import com.kiwa.domain.usecase.TryAutoLoginUseCase
import com.kiwa.domain.usecase.TryNicknameModifyUseCase
import com.kiwa.fluffit.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val checkAccessTokenUseCase: CheckAccessTokenUseCase,
    private val tryAutoLoginUseCase: TryAutoLoginUseCase,
    private val getNaverIdUseCase: GetNaverIdUseCase,
    private val signInNaverUseCase: SignInNaverUseCase,
    private val checkUserProfileUseCase: CheckUserProfileUseCase,
    private val tryNicknameModifyUseCase: TryNicknameModifyUseCase,
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

            is LoginViewEvent.TryNicknameSetting -> setState { LoginViewState.SignIn() }
            is LoginViewEvent.AttempToModifyNickname -> tryNicknameModify(event.name)
            LoginViewEvent.FinishProfileCheck -> setState { LoginViewState.LoginSuccess() }
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
                checkUserProfile()
//                setState { LoginViewState.LoginSuccess() }
            },
            onFailure = {
                setState { LoginViewState.Default() }
            }
        )
    }

    private suspend fun fetchUserNaverId(accessToken: String) {
        getNaverIdUseCase(accessToken).fold(
            onSuccess = {
                tryToLogin(it)
            },
            onFailure = {
                setState { showToast(it.message ?: "네이버 접속 실패") }
                setState { LoginViewState.Default() }
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
                setState { LoginViewState.ProfileCheck(userName = it) }
            },
            onFailure = {
                setState { showToast(it.message ?: "네트워크 에러") }
            }
        )
    }

    private suspend fun tryNicknameModify(name: String) {
        val accessToken = tokenManager.getAccessToken()

        tryNicknameModifyUseCase("Bearer $accessToken", name).fold(
            onSuccess = {
                setState { LoginViewState.LoginSuccess() }
            },
            onFailure = {
                setState { showToast(it.message ?: "중복이거나\n유효하지 않은 닉네임입니다.") }
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
            is LoginViewState.ProfileCheck -> copy(toastMessage = message)
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
            is LoginViewState.ProfileCheck -> copy(toastMessage = "")
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
                is LoginViewState.ProfileCheck -> copy(shouldExit = true)
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

            is LoginViewState.ProfileCheck -> copy(
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
