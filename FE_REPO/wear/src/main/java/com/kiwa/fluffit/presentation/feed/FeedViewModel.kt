package com.kiwa.fluffit.presentation.feed

import androidx.lifecycle.viewModelScope
import com.kiwa.fluffit.base.BaseViewModel
import com.kiwa.fluffit.presentation.feed.usecase.FeedFlupetUseCase
import com.kiwa.fluffit.presentation.feed.usecase.LoadFoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "FeedViewModel_싸피"

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val loadFoodUseCase: LoadFoodUseCase,
    private val feedFlupetUseCase: FeedFlupetUseCase
) : BaseViewModel<FeedViewState, FeedViewEvent>() {
    private val _selectedButtonIndex = MutableStateFlow<Int?>(0)
    val selectedButtonIndex: StateFlow<Int?> = _selectedButtonIndex.asStateFlow()

    private val _showDescription = MutableStateFlow<Boolean?>(false)
    val showDescription: StateFlow<Boolean?> = _showDescription.asStateFlow()

    fun selectButton(index: Int) {
        _selectedButtonIndex.value = index
    }

    fun turnOnDescription() {
        _showDescription.value = true
    }

    fun turnOffDescription() {
        _showDescription.value = false
    }

    override fun createInitialState(): FeedViewState = FeedViewState.Init()

    override fun onTriggerEvent(event: FeedViewEvent) {
        setEvent(event)
    }

    init {
        viewModelScope.launch {
            uiEvent.collect { event ->
                feedViewEvent(event)
            }
        }
    }

    private fun feedViewEvent(event: FeedViewEvent) {
        when (event) {
            FeedViewEvent.InitLoadingFood -> tryLoadFood()
            is FeedViewEvent.SelectButton -> setState { changeFeed(event.foodId) }
            is FeedViewEvent.FeedSelectedFood -> tryFeedFlupet(event.foodId)
            FeedViewEvent.OnDismissToast -> setState { onDismissToast() }
        }
    }

    private fun tryLoadFood() {
        viewModelScope.launch {
            loadFoodUseCase().fold(
                onSuccess = { foodInfo ->
                    setState {
                        FeedViewState.Default(
                            foodList = foodInfo.foods,
                            coin = foodInfo.coin
                        )
                    }
                },
                onFailure = {
                    setState {
                        FeedViewState.Default(
                            foodList = this.foodList,
                            coin = this.coin,
                            message = "먹이 불러오기 실패"
                        )
                    }
                }
            )
        }
    }

    private fun FeedViewState.changeFeed(foodId: Int): FeedViewState =
        when (this) {
            is FeedViewState.Default -> this.copy(feedNum = foodId)
            is FeedViewState.Init -> this.copy(feedNum = foodId)
            is FeedViewState.SelectFood -> this.copy(feedNum = foodId)
        }

    private fun tryFeedFlupet(foodId: Int) {
        viewModelScope.launch {
            feedFlupetUseCase(foodId).fold(
                onSuccess = {
                    setState {
                        FeedViewState.Default(
                            foodList = this.foodList,
                            coin = it.totalCoin,
                            feedNum = foodId - 1,
                            message = "남은 코인 : " + it.totalCoin.toString()
                        )
                    }

                },
                onFailure = {
                    setState {
                        FeedViewState.Default(
                            foodList = this.foodList,
                            coin = this.coin,
                            feedNum = foodId - 1,
                            message = "먹이 주기 실패"
                        )
                    }
                }
            )
        }
    }

    private fun FeedViewState.onDismissToast(): FeedViewState =
        when (this) {
            is FeedViewState.Default -> this.copy(message = "")
            is FeedViewState.Init -> this.copy(message = "")
            is FeedViewState.SelectFood -> this.copy(message = "")
        }
}
