package com.kiwa.fluffit.presentation.api

import android.util.Log
import com.google.gson.Gson
import com.kiwa.fluffit.BuildConfig
import com.kiwa.fluffit.model.battle.MatchingResponse
import com.kiwa.fluffit.presentation.token.TokenRepository
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
    private val tokenRepository: TokenRepository,
) : MatchingService {
    override suspend fun getMatching(): Result<MatchingResponse> {
        val url = "${BuildConfig.BASE_URL}battle-service/wait"
        var result: Result<MatchingResponse>? = null
        try {
            result = suspendCancellableCoroutine<Result<MatchingResponse>> { continuation ->
                val listener = object : EventSourceListener() {
                    override fun onClosed(eventSource: EventSource) {
                        super.onClosed(eventSource)
                    }

                    override fun onEvent(
                        eventSource: EventSource,
                        id: String?,
                        type: String?,
                        data: String,
                    ) {
                        super.onEvent(eventSource, id, type, data)
                        Log.d("확인-1", eventSource.toString())
                        Log.d("확인-1", id.toString())
                        Log.d("확인-1", type.toString())
                        Log.d("확인-1", data)

                        if (type == "error") {
                            val errorResponse = Gson().fromJson(data, ErrorResponse::class.java)
                            Log.d("확인-1", errorResponse.toString())
                            continuation.resumeWith(Result.failure(Exception(errorResponse.errorMessage)))
                        } else {
                            val matchingResponse = Gson().fromJson(data, MatchingResponse::class.java)
                            continuation.resumeWith(Result.success(Result.success(matchingResponse)))
                        }
                    }

                    override fun onFailure(
                        eventSource: EventSource,
                        t: Throwable?,
                        response: Response?,
                    ) {
                        super.onFailure(eventSource, t, response)
                        Log.d("확인", eventSource.request().toString())
                        Log.d("확인", t.toString())
                        Log.d("확인", response.toString())
                        continuation.resumeWith(Result.failure(Exception(response?.message)))
                    }

                    override fun onOpen(eventSource: EventSource, response: Response) {
                        super.onOpen(eventSource, response)
                    }
                }
                val accessToken = tokenRepository.accessToken.value
                val request = Request.Builder().header(
                    "Authorization",
                    "Bearer $accessToken"
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
