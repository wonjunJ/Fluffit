package com.kiwa.fluffit.presentation.token

import android.content.Context
import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import com.google.gson.Gson
import com.kiwa.fluffit.presentation.exercise.ExerciseViewModel
import kotlinx.coroutines.launch

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
            Log.d(TAG, "워치에서 받았습니다.")

            val jsonData = String(messageEvent.data, Charsets.UTF_8)
            val tokens = Gson().fromJson(jsonData, TokenData::class.java)

            //here
            tokenRepository.updateTokens(tokens.accessToken, tokens.refreshToken)

            Log.d(TAG, "onMessageReceived: accessToken:${tokens.accessToken},\nrefreshtoken:${tokens.refreshToken}")
        }
    }
}

