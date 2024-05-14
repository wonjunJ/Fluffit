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

private const val TAG = "TokenViewModel"
@HiltViewModel
class TokenViewModel @Inject constructor(
    private val messageClient: MessageClient,
    private val nodeClient: NodeClient,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _nodes = MutableLiveData<List<Node>>()
    val nodes: LiveData<List<Node>> = _nodes

    val accessToken: LiveData<String> = tokenRepository.accessToken
    val refreshToken: LiveData<String> = tokenRepository.refreshToken


    fun fetchConnectedNodes() {
        nodeClient.connectedNodes.addOnSuccessListener { nodes ->
            _nodes.postValue(nodes)
            Log.d(TAG, "연결 기기: ${nodes[0].displayName}")
        }.addOnFailureListener {
            Log.e("Node Info", "Failed to get connected nodes", it)
        }
    }

    fun requestAccessToken() {
        val path = "/request_token"
        val emptyPayload = ByteArray(0)

        nodes.value!!.forEach {
            Log.d(TAG, "토큰 요청 : ${it.id}, ${it.displayName}")

            messageClient.sendMessage(it.id, path, emptyPayload).addOnSuccessListener {
                Log.d("TokenViewModel", "Request for AccessToken sent successfully")
            }.addOnFailureListener {
                Log.e("TokenViewModel", "Failed to send request for AccessToken", it)
            }
        }


    }
}
