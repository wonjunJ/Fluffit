package com.kiwa.data.api

import com.kiwa.fluffit.model.flupet.response.Flupets
import retrofit2.http.GET
import retrofit2.http.Header

interface CollectionService {
    @GET("flupet-service/flupet/collection")
    suspend fun loadCollection(
//        @Header("Authorization") accessToken: String
    ): Flupets
}
