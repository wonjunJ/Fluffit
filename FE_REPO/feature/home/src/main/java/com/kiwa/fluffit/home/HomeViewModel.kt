package com.kiwa.fluffit.home

import androidx.lifecycle.viewModelScope
import com.kiwa.fluffit.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeViewState, HomeViewEvent>() {
    override fun createInitialState(): HomeViewState =
        HomeViewState.Default()

    override fun onTriggerEvent(event: HomeViewEvent) {
        setEvent(event)
    }

    init {
        viewModelScope.launch {
            uiEvent.collect { event ->
                when (event) {
                    HomeViewEvent.OnClickCollectionButton -> TODO()
                    is HomeViewEvent.OnClickConfirmEditButton -> setState {
                        onConfirmName(
                            event.name
                        )
                    }

                    HomeViewEvent.OnClickPencilButton -> setState { onStartEditName() }
                    HomeViewEvent.OnClickUserButton -> TODO()
                    HomeViewEvent.OnUpdateFullness -> TODO()
                    HomeViewEvent.OnUpdateHealth -> TODO()
                }
            }
        }
    }

    private fun HomeViewState.dismissRankingDialog(): HomeViewState =
        when (this) {
            is HomeViewState.Default -> this
            is HomeViewState.FlupetNameEdit -> this
            is HomeViewState.Loading -> this
        }

    private fun HomeViewState.onStartEditName(): HomeViewState =
        when (this) {
            is HomeViewState.Default -> HomeViewState.FlupetNameEdit(
                coin = this.coin,
                flupet = this.flupet,
                nextFullnessUpdateTime = this.nextFullnessUpdateTime,
                nextHealthUpdateTime = this.nextHealthUpdateTime,
                message = this.message
            )

            is HomeViewState.FlupetNameEdit -> this
            is HomeViewState.Loading -> this
        }

    private fun HomeViewState.onConfirmName(name: String): HomeViewState =
        when (this) {
            is HomeViewState.Default -> this
            is HomeViewState.FlupetNameEdit -> HomeViewState.Default(
                coin = this.coin,
                flupet = this.flupet.copy(name = name),
                nextFullnessUpdateTime = this.nextFullnessUpdateTime,
                nextHealthUpdateTime = this.nextHealthUpdateTime
            )

            is HomeViewState.Loading -> this
        }
}
