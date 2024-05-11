package com.kiwa.data.api

import com.kiwa.fluffit.model.flupet.response.CollectionResponse
import retrofit2.http.GET

interface CollectionService {
    @GET("api/flupet/collection")
    suspend fun loadCollection(
//        @Header("Authorization") accessToken: String
    ): CollectionResponse
}
