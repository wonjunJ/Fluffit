package com.kiwa.data.datasource

import com.kiwa.fluffit.model.flupet.response.Flupets

interface CollectionDataSource {
    suspend fun loadCollection(): Result<Flupets>
}
