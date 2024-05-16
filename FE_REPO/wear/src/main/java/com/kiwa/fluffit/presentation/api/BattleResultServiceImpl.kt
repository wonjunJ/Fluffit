package com.kiwa.fluffit.presentation.api

import android.util.Log
import com.google.gson.Gson
import com.kiwa.fluffit.BuildConfig
import com.kiwa.fluffit.model.battle.BattleResultRequest
import com.kiwa.fluffit.model.battle.BattleResultResponse
import com.kiwa.fluffit.presentation.token.TokenRepository
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
    private val tokenRepository: TokenRepository
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
                        continuation.resumeWith(Result.failure(Exception()))
                    }

                    override fun onOpen(eventSource: EventSource, response: Response) {
                        super.onOpen(eventSource, response)
                    }
                }
                val accessToken = tokenRepository.accessToken.value
                val json = Gson().toJson(BattleResultRequest(battleId, score))
                val requestJson = "application/json; charset=utf-8".toMediaTypeOrNull()
                val requestBody: RequestBody = json.toRequestBody(requestJson)
                val request = Request.Builder().header(
                    "Authorization",
                    "Bearer $accessToken"
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
