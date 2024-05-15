package com.kiwa.data.api

import android.util.Log
import com.google.gson.Gson
import com.kiwa.domain.TokenManager
import com.kiwa.fluffit.data.BuildConfig
import com.kiwa.fluffit.model.battle.MatchingResponse
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import javax.inject.Inject

class MatchingServiceImpl @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val tokenManager: TokenManager,
) : MatchingService {
    override suspend fun getMatching(): Result<MatchingResponse> {
        val url = "${BuildConfig.BASE_URL}battle-service/wait"
        var result: Result<MatchingResponse>? = null
        try {
            result = suspendCancellableCoroutine<Result<MatchingResponse>> { continuation ->
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
                        data: String,
                    ) {
                        super.onEvent(eventSource, id, type, data)
                        Log.d("확인", eventSource.toString())
                        Log.d("확인", id.toString())
                        Log.d("확인", type.toString())
                        Log.d("확인", data)
                        val matchingResponse = Gson().fromJson(data, MatchingResponse::class.java)
                        continuation.resumeWith(Result.success(Result.success(matchingResponse)))
                    }

                    override fun onFailure(
                        eventSource: EventSource,
                        t: Throwable?,
                        response: Response?,
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
                val request = Request.Builder().header(
                    "Authorization",
                    "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZGYyM2ZhYi1hZjQwLTQ0ZjEtYTY4" +
                        "My1mMDcyYzgxN2IyNTAiLCJpYXQiOjE3MTU3ODAzODksImV4cCI6MTcxNjM4NTE4OX0.6kB8sJQHhN9_Uv3hC5cwxt39_F45XneGBoUXL6wzRs4"
                ).url(url).build()
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
