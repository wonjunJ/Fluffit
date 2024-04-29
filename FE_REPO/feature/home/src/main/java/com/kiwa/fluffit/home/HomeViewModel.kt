package com.kiwa.fluffit.home

import com.kiwa.fluffit.home.composebase.BaseViewModel
import com.kiwa.fluffit.model.Flupet
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : BaseViewModel<HomeViewState, HomeViewEvent>() {
    override fun createInitialState(): HomeViewState =
        HomeViewState.Default()


    override fun onTriggerEvent(event: HomeViewEvent) {
        setEvent(event)
    }
}