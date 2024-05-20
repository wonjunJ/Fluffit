package com.kiwa.fluffit.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiwa.fluffit.presentation.api.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {
    private val _fullness = MutableStateFlow(0)
    val fullness: StateFlow<Int> = _fullness

    private val _health = MutableStateFlow(0)
    val health: StateFlow<Int> = _health

    private val _flupetName = MutableStateFlow("")
    val flupetName: StateFlow<String> = _flupetName

    private val _imageUrl = MutableStateFlow(emptyList<String>())
    val imageUrl: StateFlow<List<String>> = _imageUrl

    private val _birthDay = MutableStateFlow("0")
    val birthDay: StateFlow<String> = _birthDay

    private val _age = MutableStateFlow("0")
    val age: StateFlow<String> = _age

    private val _nextFullnessUpdateTime = MutableStateFlow(0L)
    val nextFullnessUpdateTime: StateFlow<Long> = _nextFullnessUpdateTime

    private val _nextHealthUpdateTime = MutableStateFlow(0L)
    val nextHealthUpdateTime: StateFlow<Long> = _nextHealthUpdateTime

    private val _coin = MutableStateFlow(0)
    val coin: StateFlow<Int> = _coin

    fun setCoin(coin : Int) {
        _coin.value = coin
    }

    fun patRequest(callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                apiRepository.patRequest()
            }
            callback(result)
        }
    }

    fun loadFlupetStatus() {
        viewModelScope.launch {
            val status = apiRepository.fetchFlupetStatus()
            if (status != null) {
                _fullness.value = status.fullness
                _health.value = status.health
                _flupetName.value = status.flupetName
                _imageUrl.value = status.imageUrl
                _birthDay.value = status.birthDay
                _age.value = status.age
                _nextFullnessUpdateTime.value = status.nextFullnessUpdateTime
                _nextHealthUpdateTime.value = status.nextHealthUpdateTime
                _coin.value = status.coin
            }
        }
    }
}
