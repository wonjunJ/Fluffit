package com.kiwa.fluffit.collection

import androidx.lifecycle.viewModelScope
import com.kiwa.domain.TokenManager
import com.kiwa.domain.usecase.LoadCollectionUseCase
import com.kiwa.fluffit.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val loadCollectionUseCase: LoadCollectionUseCase,
    private val tokenManager: TokenManager
) : BaseViewModel<CollectionViewState, CollectionViewEvent>() {
    override fun createInitialState(): CollectionViewState =
        CollectionViewState.Init()

    override fun onTriggerEvent(event: CollectionViewEvent) {
        setEvent(event)
    }

    init {
        viewModelScope.launch {
            uiEvent.collect { event ->
                collectionViewEvent(event)
            }
        }
    }

    private fun collectionViewEvent(event: CollectionViewEvent) {
        when (event) {
            CollectionViewEvent.initLoadingCollections -> tryLoadCollections()
            CollectionViewEvent.OnFinishToast -> setState { onFinishToast() }
        }
    }

    private fun tryLoadCollections() {
        viewModelScope.launch {
            loadCollectionUseCase().fold(
                onSuccess = { collectionList ->
                    setState { CollectionViewState.Default(collectionList) }
                },
                onFailure = {
                    setState { showToast("도감 기록을 가져오는 데 실패했습니다.") }
                }
            )
        }
    }

    private fun CollectionViewState.showToast(message: String): CollectionViewState =
        when (this) {
            is CollectionViewState.Init -> this.copy(toastMessage = message)
            is CollectionViewState.Default -> this.copy(toastMessage = message)
        }

    private fun CollectionViewState.onFinishToast(): CollectionViewState =
        when (this) {
            is CollectionViewState.Init -> this.copy(toastMessage = "")
            is CollectionViewState.Default -> this.copy(toastMessage = "")
        }
}
