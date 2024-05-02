package com.kiwa.fluffit.collection

import com.kiwa.fluffit.home.composebase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(

) : BaseViewModel<CollectionViewState, CollectionViewEvent>() {
    override fun createInitialState(): CollectionViewState =
        CollectionViewState.Init()

    override fun onTriggerEvent(event: CollectionViewEvent) {
        setEvent(event)
    }


}
