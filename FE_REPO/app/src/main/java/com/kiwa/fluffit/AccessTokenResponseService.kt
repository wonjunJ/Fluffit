package com.kiwa.fluffit

import android.util.Log
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import com.kiwa.domain.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val TAG = "AccessTokenResponseServ"
@AndroidEntryPoint
class AccessTokenResponseService () : WearableListenerService(), CoroutineScope {
    @Inject lateinit var tokenManager: TokenManager
    private lateinit var messageClient: MessageClient
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    override fun onCreate() {
        super.onCreate()
        messageClient = Wearable.getMessageClient(this)
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == "/request_access_token") {
            val nodeId = messageEvent.sourceNodeId
            Log.d(TAG, "onMessageReceived: 받았습니다.")

            launch {
                sendAccessToken(nodeId)
            }
        }
    }

    private suspend fun sendAccessToken(nodeId: String) {
        val path = "/access_token"
        val accessToken = tokenManager.getAccessToken() // 실제 AccessToken을 가져오는 로직
        val data = accessToken.toByteArray(Charsets.UTF_8)

        messageClient.sendMessage(nodeId, path, data).addOnSuccessListener {
            Log.d("MobileService", "토큰 : ${accessToken}")
        }.addOnFailureListener {
            Log.e("MobileService", "Failed to send AccessToken", it)
        }
    }
}
