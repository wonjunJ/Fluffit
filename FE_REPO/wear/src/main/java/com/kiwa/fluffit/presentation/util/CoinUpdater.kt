package com.kiwa.fluffit.presentation.util

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.Wearable

internal fun sendMessageToPhone(context: Context) {
    val nodeClient = Wearable.getNodeClient(context)
    nodeClient.connectedNodes.addOnSuccessListener { nodes ->
        for (node in nodes) {
            val messageClient = Wearable.getMessageClient(context)
            val task: Task<Int> =
                messageClient.sendMessage(node.id, "coin", "".toByteArray())
            task.addOnSuccessListener {
                Log.d("확인", "전송 성공")
            }.addOnFailureListener {
                Log.d("확인", "전송 실패")
            }
        }
    }
}
