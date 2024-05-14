package com.kiwa.fluffit

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import com.google.gson.Gson
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
        if (messageEvent.path == "/request_token") {
            Log.d(TAG, "onMessageReceived: 받았습니다.")

            Log.d(TAG, "답신: id- ${messageEvent.sourceNodeId}")
            launch {
                sendAccessToken()
            }
        }
    }

    private suspend fun sendAccessToken() {
        val path = "/token"
        val accessToken = tokenManager.getAccessToken()
        val refreshToken = tokenManager.getRefreshToken()

        val tokens = mapOf("accessToken" to accessToken, "refreshToken" to refreshToken)
        val jsonData = Gson().toJson(tokens)
        val data = jsonData.toByteArray(Charsets.UTF_8)

        val nodeClient = Wearable.getNodeClient(this)
        nodeClient.connectedNodes.addOnSuccessListener { nodes ->
            for (node in nodes) {
                Log.d(TAG, "보내기: ${node.displayName}, ${node.id}")
                messageClient.sendMessage(node.id, path, data).addOnSuccessListener {
                    Log.d("MobileService", "토큰 : ${tokens}")
                }.addOnFailureListener {
                    Log.e("MobileService", "Failed to send AccessToken", it)
                }
            }
        }

    }

}
