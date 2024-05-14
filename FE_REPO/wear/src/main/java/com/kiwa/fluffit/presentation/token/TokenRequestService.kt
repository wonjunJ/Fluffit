package com.kiwa.fluffit.presentation.token

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService

private const val TAG = "TokenRequestService 이창곤"
class TokenRequestService : WearableListenerService() {
    private lateinit var messageClient: MessageClient

    override fun onCreate() {
        super.onCreate()
        messageClient = Wearable.getMessageClient(this)
    }

    fun requestAccessToken(nodeId: String) {
        val path = "/request_access_token"
        val emptyPayload = ByteArray(0)

        messageClient.sendMessage(nodeId, path, emptyPayload).addOnSuccessListener {
            Log.d(TAG, "Request for AccessToken sent successfully")
        }.addOnFailureListener {
            Log.e("WearService", "Failed to send request for AccessToken", it)
        }
    }
}

fun fetchConnectedNodes(context: Context) {
    val nodeClient: NodeClient = Wearable.getNodeClient(context)

    nodeClient.connectedNodes.addOnSuccessListener { nodes: List<Node> ->
        for (node in nodes) {
            Log.d("Node Info", "Node ID: ${node.id} - Node Name: ${node.displayName}")
            // 여기서 필요한 노드 ID를 사용할 수 있습니다.
        }

    }.addOnFailureListener {
        Log.e("Node Info", "Failed to get connected nodes")
    }
}
