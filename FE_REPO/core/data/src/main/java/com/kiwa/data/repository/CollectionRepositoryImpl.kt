package com.kiwa.data.repository

import com.kiwa.data.datasource.CollectionDataSource
import com.kiwa.domain.TokenManager
import com.kiwa.domain.repository.CollectionRepository
import com.kiwa.fluffit.model.flupet.response.Collections
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(
    private val collectionDataSource: CollectionDataSource,
    private val tokenManager: TokenManager
) : CollectionRepository {

    override suspend fun loadCollection(): Result<List<Collections>> =
        collectionDataSource.loadCollection().fold(
            onSuccess = { response ->
                Result.success(
                    response.flupets.map {
                        Collections(
                            it.species,
                            it.imageUrl,
                            it.tier,
                            it.metBefore
                        )
                    }
                )
            },
            onFailure = {
                Result.failure(it)
            }
        )
}
