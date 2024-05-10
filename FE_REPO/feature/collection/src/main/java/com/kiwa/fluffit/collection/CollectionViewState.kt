package com.kiwa.fluffit.collection

import com.kiwa.fluffit.base.ViewState
import com.kiwa.fluffit.model.FlupetCollection

sealed class CollectionViewState : ViewState {
    abstract val isLoadingCollected: Boolean
    abstract val toastMessage: String

    data class Init(
        override val isLoadingCollected: Boolean = true,
        override val toastMessage: String = ""
    ) : CollectionViewState()

    data class Default(
        val collectionList: List<FlupetCollection>,
        override val isLoadingCollected: Boolean = false,
        override val toastMessage: String = ""
    ) : CollectionViewState()
}
