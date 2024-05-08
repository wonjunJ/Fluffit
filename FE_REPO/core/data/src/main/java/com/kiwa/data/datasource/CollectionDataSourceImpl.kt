package com.kiwa.data.datasource

import com.kiwa.data.api.CollectionService
import com.kiwa.fluffit.model.flupet.response.CollectionResponse
import javax.inject.Inject

class CollectionDataSourceImpl @Inject constructor(
    private val collectionService: CollectionService
) : CollectionDataSource {
//    override suspend fun loadCollection(accessToken: String): Result<CollectionResponse> =
    override suspend fun loadCollection(): Result<CollectionResponse> =
        runCatching {
//            collectionService.loadCollection(accessToken)
            collectionService.loadCollection()
        }
}
