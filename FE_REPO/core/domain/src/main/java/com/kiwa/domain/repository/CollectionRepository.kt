package com.kiwa.domain.repository

import com.kiwa.fluffit.model.flupet.response.Collections

interface CollectionRepository {
    suspend fun loadCollection(): Result<List<Collections>>
}
