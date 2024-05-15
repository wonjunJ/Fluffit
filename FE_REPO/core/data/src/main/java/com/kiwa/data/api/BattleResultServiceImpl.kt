package com.kiwa.data.api

import okhttp3.OkHttpClient
import javax.inject.Inject

class BattleResultServiceImpl @Inject constructor(
    private val okHttpClient: OkHttpClient
) : BattleResultService {
    override fun getBattleResult() {
        TODO("Not yet implemented")
    }
}
