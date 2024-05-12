package com.kiwa.fluffit.mypage

import androidx.lifecycle.viewModelScope
import com.kiwa.domain.TokenManager
import com.kiwa.domain.usecase.CheckUserProfileUseCase
import com.kiwa.domain.usecase.LogOutUseCase
import com.kiwa.domain.usecase.SignOutUseCase
import com.kiwa.domain.usecase.TryNicknameModifyUseCase
import com.kiwa.fluffit.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val checkUserProfileUseCase: CheckUserProfileUseCase,
    private val tryNicknameModifyUseCase: TryNicknameModifyUseCase,
    private val tokenManager: TokenManager
) : BaseViewModel<MyPageViewState, MyPageViewEvent>() {
    override fun createInitialState(): MyPageViewState = MyPageViewState.Init()

    override fun onTriggerEvent(event: MyPageViewEvent) {
        setEvent(event)
    }

    init {
        viewModelScope.launch {
            uiEvent.collect { event ->
                myPageViewEvent(event)
            }
        }
    }

    private suspend fun myPageViewEvent(event: MyPageViewEvent) {
        when (event) {
            MyPageViewEvent.Initialize -> tryLoadUserName()

            MyPageViewEvent.OnClickPencil -> setState { onStartEditUserName() }
            is MyPageViewEvent.OnClickModifyUserName -> startSaveNewUserName(event.newName)

            MyPageViewEvent.OnClickLogout -> logout()
            is MyPageViewEvent.OnClickSignOut -> setState { tryingSignOut() }
            is MyPageViewEvent.OnClickSignOutConfirm -> signOut(event.naverAccessToken)
            MyPageViewEvent.OnCancelSignOut -> setState { cancelSignOut() }

            MyPageViewEvent.OnFinishToast -> setState { onFinishToast() }
            is MyPageViewEvent.SetChangedUserName -> startSaveNewUserName(event.newUserName)
        }
    }

    private fun MyPageViewState.onStartEditUserName(): MyPageViewState =
        when (this) {
            is MyPageViewState.Default -> MyPageViewState.EditName(
                name = this.name
            )

            is MyPageViewState.EditName -> this
            is MyPageViewState.Init -> MyPageViewState.EditName(
                name = this.name
            )
        }

    private suspend fun startSaveNewUserName(name: String) {
        val accessToken = tokenManager.getAccessToken()

        tryNicknameModifyUseCase("Bearer $accessToken", name).fold(
            onSuccess = {
                setState { MyPageViewState.Init() }
            },
            onFailure = {
                setState { showToast(it.message ?: "중복이거나\n유효하지 않은 닉네임입니다.") }
            }
        )
    }

    private fun tryLoadUserName() {
        viewModelScope.launch {
            val accessToken = tokenManager.getAccessToken()
            checkUserProfileUseCase(accessToken).fold(
                onSuccess = { user ->
                    setState { MyPageViewState.Default(name = user) }
                },
                onFailure = {
                    setState { showToast("유저 이름을 가져오는 데 실패했습니다.") }
                }
            )
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logOutUseCase().fold(
                onSuccess = {
                    setState { onSuccessLogout() }
                },
                onFailure = {
                    setState { showToast("로그아웃에 실패했습니다!") }
                }
            )
        }
    }

    private fun MyPageViewState.onSuccessLogout(): MyPageViewState =
        when (this) {
            is MyPageViewState.Default -> this.copy(isLogin = false)
            is MyPageViewState.Init -> this.copy(isLogin = false)
            is MyPageViewState.EditName -> this.copy(isLogin = false)
        }

    private fun MyPageViewState.tryingSignOut(): MyPageViewState =
        when (this) {
            is MyPageViewState.Default -> this.copy(isTryingSignOut = true)
            is MyPageViewState.Init -> this.copy(isTryingSignOut = true)
            is MyPageViewState.EditName -> this.copy(isTryingSignOut = true)
        }

    private fun signOut(accessToken: String) {
        viewModelScope.launch {
            signOutUseCase(
                BuildConfig.NAVER_LOGIN_CLIENT_ID,
                BuildConfig.NAVER_LOGIN_CLIENT_SECRET,
                accessToken
            ).fold(
                onSuccess = {
                    logout()
                },
                onFailure = {
                    setState { showToast("회원탈퇴에 실패했습니다.") }
                }
            )
        }
    }

    private fun MyPageViewState.cancelSignOut(): MyPageViewState =
        when (this) {
            is MyPageViewState.Default -> copy(isTryingSignOut = false)
            is MyPageViewState.Init -> copy(isTryingSignOut = false)
            is MyPageViewState.EditName -> copy(isTryingSignOut = false)
        }

    private fun MyPageViewState.showToast(message: String): MyPageViewState =
        when (this) {
            is MyPageViewState.Default -> this.copy(toastMessage = message)
            is MyPageViewState.Init -> this.copy(toastMessage = message)
            is MyPageViewState.EditName -> this.copy(toastMessage = message)
        }

    private fun MyPageViewState.onFinishToast(): MyPageViewState =
        when (this) {
            is MyPageViewState.Default -> this.copy(toastMessage = "")
            is MyPageViewState.Init -> this.copy(toastMessage = "")
            is MyPageViewState.EditName -> this.copy(toastMessage = "")
        }
}
