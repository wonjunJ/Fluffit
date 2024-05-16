package com.kiwa.fluffit.flupet.history

import androidx.lifecycle.viewModelScope
import com.kiwa.domain.usecase.LoadFlupetHistoryUseCase
import com.kiwa.fluffit.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlupetHistoryViewModel @Inject constructor(
    private val loadFlupetHistoryUseCase: LoadFlupetHistoryUseCase
) : BaseViewModel<FlupetHistoryViewState, FlupetHistoryViewEvent>() {
    override fun createInitialState(): FlupetHistoryViewState =
        FlupetHistoryViewState.Init()

    override fun onTriggerEvent(event: FlupetHistoryViewEvent) {
        setEvent(event)
    }

    init {
        viewModelScope.launch {
            uiEvent.collect { event ->
                flupetHistoryViewEvent(event)
            }
        }
    }

    private fun flupetHistoryViewEvent(event: FlupetHistoryViewEvent) {
        when (event) {
            FlupetHistoryViewEvent.initLoadingHistory -> tryLoadHistory()
            FlupetHistoryViewEvent.OnFinishToast -> setState { onFinishToast() }
        }
    }

    private fun tryLoadHistory() {
        viewModelScope.launch {
            loadFlupetHistoryUseCase().fold(
                onSuccess = { flupetHistoryList ->
                    setState { FlupetHistoryViewState.Default(flupetHistoryList) }
                },
                onFailure = {
                    setState { showToast("플러펫 기록이 없습니다!") }
                }
            )
        }
    }

    private fun FlupetHistoryViewState.showToast(message: String): FlupetHistoryViewState =
        when (this) {
            is FlupetHistoryViewState.Init -> this.copy(toastMessage = message)
            is FlupetHistoryViewState.Default -> this.copy(toastMessage = message)
        }

    private fun FlupetHistoryViewState.onFinishToast(): FlupetHistoryViewState =
        when (this) {
            is FlupetHistoryViewState.Init -> this.copy(toastMessage = "")
            is FlupetHistoryViewState.Default -> this.copy(toastMessage = "")
        }
}
