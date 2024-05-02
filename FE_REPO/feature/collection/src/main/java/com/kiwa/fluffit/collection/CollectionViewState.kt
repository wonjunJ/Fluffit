package com.kiwa.fluffit.collection

import com.kiwa.fluffit.home.composebase.ViewState

sealed class CollectionViewState : ViewState {

    abstract val isLoadingCollected : Boolean

    data class Init(
        override val isLoadingCollected: Boolean = true
    ) : CollectionViewState()

    data class Default(
        override val isLoadingCollected: Boolean = false
    ) : CollectionViewState()
}
