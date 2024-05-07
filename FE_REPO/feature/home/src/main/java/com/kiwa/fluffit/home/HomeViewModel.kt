package com.kiwa.fluffit.home

import androidx.lifecycle.viewModelScope
import com.kiwa.domain.usecase.GetMainUIInfoUseCase
import com.kiwa.fluffit.base.BaseViewModel
import com.kiwa.fluffit.model.main.MainUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMainUIInfoUseCase: GetMainUIInfoUseCase
) : BaseViewModel<HomeViewState, HomeViewEvent>() {
    override fun createInitialState(): HomeViewState =
        HomeViewState.Default()

    override fun onTriggerEvent(event: HomeViewEvent) {
        setEvent(event)
    }

    init {
        viewModelScope.launch {
            getMainUIInfo()
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

    private suspend fun getMainUIInfo(){
        getMainUIInfoUseCase().fold(
            onSuccess = { setState { showMainUIInfo(it) } },
            onFailure = {}
        )
    }

    private fun showMainUIInfo(mainUIModel: MainUIModel): HomeViewState =
        HomeViewState.Default(
            coin = mainUIModel.coin,
            flupet = mainUIModel.flupet,
            nextFullnessUpdateTime = mainUIModel.nextFullnessUpdateTime,
            nextHealthUpdateTime = mainUIModel.nextHealthUpdateTime
        )

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
        }
}
