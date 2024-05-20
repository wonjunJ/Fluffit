package com.kiwa.fluffit.presentation.token

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TokenRepository(private val sharedPreferences: SharedPreferences) {

    private val _accessToken = MutableLiveData<String>()
    val accessToken: LiveData<String> get() = _accessToken

    private val _refreshToken = MutableLiveData<String>()
    val refreshToken: LiveData<String> get() = _refreshToken

    init {
        loadTokens()
    }

    private fun loadTokens() {
        _accessToken.postValue(sharedPreferences.getString("ACCESS_TOKEN", null))
        _refreshToken.postValue(sharedPreferences.getString("REFRESH_TOKEN", null))
    }

    fun updateTokens(accessToken: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            putString("ACCESS_TOKEN", accessToken)
            putString("REFRESH_TOKEN", refreshToken)
            apply()
        }
        _accessToken.postValue(accessToken)
        _refreshToken.postValue(refreshToken)
    }
}
