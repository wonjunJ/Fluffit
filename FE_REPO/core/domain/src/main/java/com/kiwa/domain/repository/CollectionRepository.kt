package com.kiwa.domain.repository

import com.kiwa.fluffit.model.flupet.FlupetCollection

interface CollectionRepository {
    suspend fun loadCollection(): Result<List<FlupetCollection>>
}
