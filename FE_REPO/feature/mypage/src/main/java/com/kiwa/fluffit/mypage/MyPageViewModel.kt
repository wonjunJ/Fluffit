package com.kiwa.fluffit.mypage

import androidx.lifecycle.viewModelScope
import com.kiwa.domain.usecase.LoadUserNameUseCase
import com.kiwa.domain.usecase.LogOutUseCase
import com.kiwa.domain.usecase.SaveNewUserNameUseCase
import com.kiwa.domain.usecase.SignOutUseCase
import com.kiwa.fluffit.base.BaseViewModel
import com.kiwa.fluffit.home.HomeViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val loadUserNameUseCase: LoadUserNameUseCase,
    private val saveNewUserNameUseCase: SaveNewUserNameUseCase
) : BaseViewModel<MyPageViewState, MyPageViewEvent>() {
    override fun createInitialState(): MyPageViewState = MyPageViewState.Init()

    override fun onTriggerEvent(event: MyPageViewEvent) {
        when (event) {
            MyPageViewEvent.Initialize -> tryLoadUserName()

            MyPageViewEvent.OnClickPencil -> setState { onStartEditUserName() }
            is MyPageViewEvent.OnClickModifyUserName -> setState { onFinishEditUserName(event.newName) }

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
                toastMessage = this.toastMessage,
                isLogin = this.isLogin,
                isTryingSignOut = this.isTryingSignOut,
                isLoadingUserName = this.isLoadingUserName,
                name = this.name
            )

            is MyPageViewState.EditName -> this
            is MyPageViewState.Init -> MyPageViewState.EditName(
                toastMessage = this.toastMessage,
                isLogin = this.isLogin,
                isTryingSignOut = this.isTryingSignOut,
                isLoadingUserName = this.isLoadingUserName,
                name = this.name
            )
        }

    private fun MyPageViewState.onFinishEditUserName(name: String): MyPageViewState =
        when (this) {
            is MyPageViewState.Default -> this
            is MyPageViewState.EditName -> MyPageViewState.Default(
                toastMessage = this.toastMessage,
                isLogin = this.isLogin,
                isTryingSignOut = this.isTryingSignOut,
                isLoadingUserName = this.isLoadingUserName,
                name = name
            )

            is MyPageViewState.Init -> this
        }

    private fun startSaveNewUserName(name: String) {
        viewModelScope.launch {
            saveNewUserNameUseCase(name).fold(
                onSuccess = {
                    setState { showToast("이름 변경에 성공했습니다.") }
                },
                onFailure = {
                    setState { showToast("이름 변경에 실패했습니다.") }
                    tryLoadUserName()
                }
            )
        }
    }

    private fun tryLoadUserName() {
        viewModelScope.launch {
            loadUserNameUseCase().fold(
                onSuccess = { user ->
                    setState { MyPageViewState.Default(name = user.userName) }
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
