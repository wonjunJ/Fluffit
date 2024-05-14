package com.kiwa.fluffit.collection

import com.kiwa.fluffit.base.ViewState
import com.kiwa.fluffit.model.flupet.response.Collections

sealed class CollectionViewState : ViewState {
    abstract val isLoadingCollected: Boolean
    abstract val toastMessage: String

    data class Init(
        override val isLoadingCollected: Boolean = true,
        override val toastMessage: String = ""
    ) : CollectionViewState()

    data class Default(
        val collectionList: List<Collections>,
        override val isLoadingCollected: Boolean = false,
        override val toastMessage: String = ""
    ) : CollectionViewState()
}
