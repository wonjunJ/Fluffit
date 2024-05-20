package com.kiwa.fluffit

import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
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

@AndroidEntryPoint
class AccessTokenResponseService() : WearableListenerService(), CoroutineScope {
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
                messageClient.sendMessage(node.id, path, data).addOnSuccessListener {
                }.addOnFailureListener {
                }
            }
        }
    }
}
