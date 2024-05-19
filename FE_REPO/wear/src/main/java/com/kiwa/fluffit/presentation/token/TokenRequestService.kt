package com.kiwa.fluffit.presentation.token

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import com.google.gson.Gson

private const val TAG = "TokenRequestService 이창곤"
class TokenRequestService : WearableListenerService() {
    private lateinit var messageClient: MessageClient
    private lateinit var tokenRepository: TokenRepository

    override fun onCreate() {
        super.onCreate()
        messageClient = Wearable.getMessageClient(this)
        val sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        tokenRepository = TokenRepository(sharedPreferences)
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == "/token") {
            val jsonData = String(messageEvent.data, Charsets.UTF_8)
            val tokens = Gson().fromJson(jsonData, TokenData::class.java)
            tokenRepository.updateTokens(tokens.accessToken, tokens.refreshToken)
        }
    }
}
