package com.kiwa.fluffit.collection

import com.kiwa.fluffit.home.composebase.ViewEvent

sealed class CollectionViewEvent : ViewEvent {
    data object initLoadingCollections : CollectionViewEvent()
}
