package com.kiwa.data.datasource

import com.kiwa.fluffit.model.flupet.response.CollectionResponse

interface CollectionDataSource {
//    suspend fun loadCollection(accessToken: String): Result<CollectionResponse>
    suspend fun loadCollection(): Result<CollectionResponse>
}
