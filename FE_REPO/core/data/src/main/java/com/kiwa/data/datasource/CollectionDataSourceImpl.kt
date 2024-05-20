package com.kiwa.data.datasource

import com.kiwa.data.api.CollectionService
import com.kiwa.fluffit.model.flupet.response.Flupets
import javax.inject.Inject

class CollectionDataSourceImpl @Inject constructor(
    private val collectionService: CollectionService
) : CollectionDataSource {
    override suspend fun loadCollection(): Result<Flupets> =
        runCatching {
            collectionService.loadCollection()
        }
}
