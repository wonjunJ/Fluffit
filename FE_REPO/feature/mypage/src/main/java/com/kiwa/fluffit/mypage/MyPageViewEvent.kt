package com.kiwa.fluffit.mypage

import com.kiwa.fluffit.base.ViewEvent

sealed class MyPageViewEvent : ViewEvent {
    data object Initialize : MyPageViewEvent()

    data object OnClickLogout : MyPageViewEvent()

    data object OnClickSignOut : MyPageViewEvent()

    data class OnClickSignOutConfirm(val naverAccessToken: String) : MyPageViewEvent()

    data object OnCancelSignOut : MyPageViewEvent()

    data object OnClickPencil : MyPageViewEvent()

    data class OnClickModifyUserName(val newName: String) : MyPageViewEvent()

    data class SetChangedUserName(val newUserName: String) : MyPageViewEvent()

    data object OnFinishToast : MyPageViewEvent()
}
