package com.kiwa.fluffit.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiwa.fluffit.presentation.api.ApiRepository
import com.kiwa.fluffit.presentation.model.FlupetStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewModel"
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {
    private val _flupetStatus = MutableLiveData<FlupetStatus>()
    val flupetStatus: LiveData<FlupetStatus> = _flupetStatus

    fun loadFlupetStatus() {
        viewModelScope.launch {
            _flupetStatus.value = apiRepository.fetchFlupetStatus()
            Log.d(TAG, "플러펫 정보: ${flupetStatus.value}")
        }
    }
}
