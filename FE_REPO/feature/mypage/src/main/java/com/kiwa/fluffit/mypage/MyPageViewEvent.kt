package com.kiwa.fluffit.mypage

import com.kiwa.fluffit.base.ViewEvent

sealed class MyPageViewEvent : ViewEvent {
    data object init : MyPageViewEvent()
}
