package com.kiwa.fluffit.collection

import com.kiwa.fluffit.home.composebase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(

) : BaseViewModel<CollectionViewState, CollectionViewEvent> {
}
