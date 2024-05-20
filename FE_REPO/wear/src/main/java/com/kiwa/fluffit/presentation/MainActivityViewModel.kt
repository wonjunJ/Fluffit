package com.kiwa.fluffit.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


object MainActivityViewModel : ViewModel() {

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage

    fun setPage(page: Int) {
        _currentPage.value = page
    }
    fun nextPage() {
        if(_currentPage.value < MainActivity.PAGE_COUNT - 1){
            viewModelScope.launch {
                _currentPage.value++
            }
        }

    }

    fun previousPage() {
        if(_currentPage.value != 0){
            viewModelScope.launch {
                _currentPage.value--
            }
        }
    }
}