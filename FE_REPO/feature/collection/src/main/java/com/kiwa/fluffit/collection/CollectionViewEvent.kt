package com.kiwa.fluffit.collection

import com.kiwa.fluffit.base.ViewEvent

sealed class CollectionViewEvent : ViewEvent {
    data object initLoadingCollections : CollectionViewEvent()

    data object afterLoadingCollections : CollectionViewEvent()

    data object OnFinishToast : CollectionViewEvent()
}
