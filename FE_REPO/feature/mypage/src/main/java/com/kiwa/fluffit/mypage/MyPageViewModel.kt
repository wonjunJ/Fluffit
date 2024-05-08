package com.kiwa.fluffit.mypage

import androidx.lifecycle.viewModelScope
import com.kiwa.fluffit.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(

) : BaseViewModel<MyPageViewState, MyPageViewEvent>() {
    override fun createInitialState(): MyPageViewState =
        MyPageViewState.Init()

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

    private fun myPageViewEvent(event: MyPageViewEvent) {
        when (event) {
            MyPageViewEvent.init -> TODO()
        }
    }


    private fun MyPageViewState.showToast(message: String): MyPageViewState =
        when (this) {
            is MyPageViewState.Init -> this.copy(toastMessage = message)
        }

    private fun MyPageViewState.onFinishToast(): MyPageViewState =
        when (this) {
            is MyPageViewState.Init -> this.copy(toastMessage = "")
        }
}
