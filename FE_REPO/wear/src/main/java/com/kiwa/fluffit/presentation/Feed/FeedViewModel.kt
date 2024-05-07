package com.kiwa.fluffit.presentation.Feed

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor() : ViewModel() {
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

    fun turnOffDescription(){
        _showDescription.value = false
    }

}