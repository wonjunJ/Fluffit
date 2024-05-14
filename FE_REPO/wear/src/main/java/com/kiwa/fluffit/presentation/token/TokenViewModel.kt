package com.kiwa.fluffit.presentation.token

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.NodeClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TokenViewModel @Inject constructor(
    private val messageClient: MessageClient,
    private val nodeClient: NodeClient,
) : ViewModel() {

    private val _nodes = MutableLiveData<List<Node>>()
    val nodes: LiveData<List<Node>> = _nodes

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    fun fetchConnectedNodes(context: Context) {
        nodeClient.connectedNodes.addOnSuccessListener { nodes ->
            _nodes.postValue(nodes)
        }.addOnFailureListener {
            Log.e("Node Info", "Failed to get connected nodes", it)
        }
    }

    fun requestAccessToken(nodeId: String) {
        val path = "/request_access_token"
        val emptyPayload = ByteArray(0)

        messageClient.sendMessage(nodeId, path, emptyPayload).addOnSuccessListener {
            Log.d("TokenViewModel", "Request for AccessToken sent successfully")
        }.addOnFailureListener {
            Log.e("TokenViewModel", "Failed to send request for AccessToken", it)
        }
    }
}
