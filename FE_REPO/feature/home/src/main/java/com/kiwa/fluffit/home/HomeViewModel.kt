package com.kiwa.fluffit.home

import androidx.lifecycle.viewModelScope
import com.kiwa.domain.usecase.GetMainUIInfoUseCase
import com.kiwa.domain.usecase.UpdateFullnessUseCase
import com.kiwa.domain.usecase.UpdateHealthUseCase
import com.kiwa.fluffit.base.BaseViewModel
import com.kiwa.fluffit.model.main.Flupet
import com.kiwa.fluffit.model.main.FullnessUpdateInfo
import com.kiwa.fluffit.model.main.HealthUpdateInfo
import com.kiwa.fluffit.model.main.MainUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMainUIInfoUseCase: GetMainUIInfoUseCase,
    private val updateFullnessUseCase: UpdateFullnessUseCase,
    private val updateHealthUseCase: UpdateHealthUseCase
) : BaseViewModel<HomeViewState, HomeViewEvent>() {
    override fun createInitialState(): HomeViewState =
        HomeViewState.Default()

    override fun onTriggerEvent(event: HomeViewEvent) {
        setEvent(event)
    }

    private lateinit var updateFullnessJob: Job
    private lateinit var updateHealthJob: Job

    private val fullnessUpdateState: MutableSharedFlow<FullnessUpdateInfo> =
        MutableSharedFlow()

    private val healthUpdateState: MutableSharedFlow<HealthUpdateInfo> =
        MutableSharedFlow()

    init {
        viewModelScope.launch {
            fullnessUpdateState.collect {
                setState { updateFullness(it) }
            }
        }
        viewModelScope.launch {
            healthUpdateState.collect {
                setState { updateHealth(it) }
            }
        }
        handleUIEvent()
        getMainUIInfo()
        viewModelScope.launch {
            updateFullnessUseCase().fold(
                onSuccess = { fullnessUpdateState.emit(it) },
                onFailure = {}
            )
        }

        viewModelScope.launch {
            updateHealthUseCase().fold(
                onSuccess = { healthUpdateState.emit(it) },
                onFailure = {}
            )
        }
    }

    private fun handleUIEvent() {
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
                    is HomeViewEvent.OnUpdateFullness -> enqueueNewRequest(
                        event.stat,
                        uiState.value.nextFullnessUpdateTime
                    )

                    is HomeViewEvent.OnUpdateHealth -> enqueueNewRequest(
                        event.stat,
                        uiState.value.nextHealthUpdateTime
                    )
                }
            }
        }
    }

    private fun HomeViewState.updateFullness(fullnessUpdateInfo: FullnessUpdateInfo): HomeViewState =
        when (this) {
            is HomeViewState.Default -> this.copy(
                nextFullnessUpdateTime = fullnessUpdateInfo.nextUpdateTime,
                flupet = this.flupet.copy(
                    fullness = fullnessUpdateInfo.fullness,
                    evolutionAvailable = fullnessUpdateInfo.isEvolutionAvailable
                )
            )

            is HomeViewState.FlupetNameEdit -> this.copy(
                nextFullnessUpdateTime = fullnessUpdateInfo.nextUpdateTime,
                flupet = this.flupet.copy(
                    fullness = fullnessUpdateInfo.fullness,
                    evolutionAvailable = fullnessUpdateInfo.isEvolutionAvailable
                )
            )
        }

    private fun HomeViewState.updateHealth(healthUpdateInfo: HealthUpdateInfo): HomeViewState =
        when (this) {
            is HomeViewState.Default -> this.copy(
                nextHealthUpdateTime = healthUpdateInfo.nextUpdateTime,
                flupet = this.flupet.copy(
                    health = healthUpdateInfo.health,
                    evolutionAvailable = healthUpdateInfo.isEvolutionAvailable
                )
            )

            is HomeViewState.FlupetNameEdit -> this.copy(
                nextHealthUpdateTime = healthUpdateInfo.nextUpdateTime,
                flupet = this.flupet.copy(
                    health = healthUpdateInfo.health,
                    evolutionAvailable = healthUpdateInfo.isEvolutionAvailable
                )
            )
        }

    private fun getMainUIInfo() {
        viewModelScope.launch {
            setState {
                showMainUIInfo(
                    MainUIModel(
                        uiState.value.coin,
                        Flupet(
                            uiState.value.flupet.fullness,
                            uiState.value.flupet.health,
                            uiState.value.flupet.imageUrl,
                            uiState.value.flupet.name,
                            uiState.value.flupet.birthDay,
                            uiState.value.flupet.age,
                            uiState.value.flupet.evolutionAvailable
                        ),
                        nextFullnessUpdateTime = uiState.value.nextFullnessUpdateTime,
                        nextHealthUpdateTime = uiState.value.nextHealthUpdateTime
                    )
                )
            }
        }
//        getMainUIInfoUseCase().fold(
//            onSuccess = { setState { showMainUIInfo(it) } },
//            onFailure = {}
//        )
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

    private suspend fun enqueueNewRequest(tag: String, nextUpdateTime: Long) {
        when (tag) {
            "fullness" -> {
                if (::updateFullnessJob.isInitialized) {
                    updateFullnessJob.cancel()
                    updateFullnessJob.join()
                }
                updateFullnessJob = viewModelScope.launch(Dispatchers.IO) {
                    val delayTime = nextUpdateTime - System.currentTimeMillis()
                    if (delayTime > 0) {
                        delay(delayTime)
                        updateFullnessUseCase().fold(
                            onSuccess = { fullnessUpdateState.emit(it) },
                            onFailure = {}
                        )
                    }
                }
            }

            "health" -> {
                if (::updateHealthJob.isInitialized) {
                    updateHealthJob.cancel()
                    updateHealthJob.join()
                }
                updateHealthJob = viewModelScope.launch(Dispatchers.IO) {
                    val delayTime = nextUpdateTime - System.currentTimeMillis()
                    if (delayTime > 0) {
                        delay(delayTime)
                        updateHealthUseCase().fold(
                            onSuccess = { healthUpdateState.emit(it) },
                            onFailure = {}
                        )
                    }
                }
            }
        }
    }
}
