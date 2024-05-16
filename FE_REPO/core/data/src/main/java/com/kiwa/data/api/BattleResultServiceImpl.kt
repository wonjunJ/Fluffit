package com.kiwa.data.api

import android.util.Log
import com.google.gson.Gson
import com.kiwa.domain.TokenManager
import com.kiwa.fluffit.data.BuildConfig
import com.kiwa.fluffit.model.battle.BattleResultRequest
import com.kiwa.fluffit.model.battle.BattleResultResponse
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import javax.inject.Inject

class BattleResultServiceImpl @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val tokenManager: TokenManager
) : BattleResultService {
    override suspend fun getBattleResult(
        battleId: String,
        score: Int
    ): Result<BattleResultResponse> {
        val url = "${BuildConfig.BASE_URL}battle-service/result"
        var result: Result<BattleResultResponse>? = null
        try {
            result = suspendCancellableCoroutine<Result<BattleResultResponse>> { continuation ->
                val listener = object : EventSourceListener() {
                    override fun onClosed(eventSource: EventSource) {
                        super.onClosed(eventSource)
                        Log.d("확인", "닫힘")
                        continuation.resumeWith(Result.failure(Exception()))
                    }

                    override fun onEvent(
                        eventSource: EventSource,
                        id: String?,
                        type: String?,
                        data: String
                    ) {
                        super.onEvent(eventSource, id, type, data)
                        Log.d("확인", eventSource.toString())
                        Log.d("확인", id.toString())
                        Log.d("확인", type.toString())
                        Log.d("확인", data)
                        val battleResultResponse =
                            Gson().fromJson(data, BattleResultResponse::class.java)
                        continuation.resumeWith(
                            Result.success(Result.success(battleResultResponse))
                        )
                    }

                    override fun onFailure(
                        eventSource: EventSource,
                        t: Throwable?,
                        response: Response?
                    ) {
                        super.onFailure(eventSource, t, response)
                        Log.d("확인", "실패")
                        continuation.resumeWith(Result.failure(Exception()))
                    }

                    override fun onOpen(eventSource: EventSource, response: Response) {
                        super.onOpen(eventSource, response)
                        Log.d("확인", "오픈")
                    }
                }
                val accessToken = runBlocking { tokenManager.getAccessToken() }
                val json = Gson().toJson(BattleResultRequest(battleId, score))
                val requestJson = "application/json; charset=utf-8".toMediaTypeOrNull()
                val requestBody: RequestBody = json.toRequestBody(requestJson)
                val request = Request.Builder().header(
                    "Authorization",
                    "Bear eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZGYyM2ZhYi1hZjQwLTQ0ZjEtYTY4My1mMDcyYzgxN2IyNTAiLCJpYXQiOjE3MTU3ODAzODksImV4cCI6MTcxNjM4NTE4OX0.6kB8sJQHhN9_Uv3hC5cwxt39_F45XneGBoUXL6wzRs4"
                ).method("POST", requestBody).url(url).build()
                val eventSource =
                    EventSources.createFactory(okHttpClient).newEventSource(request, listener)
                eventSource.request()
            }
        } catch (e: Exception) {
            result = Result.failure(e)
        }
        return result!!
    }
}
