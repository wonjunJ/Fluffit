package com.kiwa.data.api

import com.kiwa.fluffit.model.battle.MatchingResponse

interface MatchingService {
    suspend fun getMatching(): Result<MatchingResponse>
}
