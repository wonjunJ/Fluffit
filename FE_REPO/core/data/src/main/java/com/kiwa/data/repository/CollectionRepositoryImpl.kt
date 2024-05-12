package com.kiwa.data.repository

import android.util.Log
import com.kiwa.data.datasource.CollectionDataSource
import com.kiwa.domain.TokenManager
import com.kiwa.domain.repository.CollectionRepository
import com.kiwa.fluffit.model.flupet.response.Collections
import com.kiwa.fluffit.model.main.FlupetCollection
import javax.inject.Inject

private const val TAG = "CollectionRepositoryImp_싸피"
class CollectionRepositoryImpl @Inject constructor(
    private val collectionDataSource: CollectionDataSource,
    private val tokenManager: TokenManager
) : CollectionRepository {

    override suspend fun loadCollection(): Result<List<Collections>> =
        collectionDataSource.loadCollection().fold(
            onSuccess = { response ->
                Result.success(
                    response.flupets.map {
                        Log.d(TAG, "loadCollection: ${it}")
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
//                Log.d(TAG, "loadCollection: ${accessToken}")
                Log.d(TAG, "loadCollection: ${it}")
                Log.d(TAG, "loadCollection: 실패")
                Result.failure(it)
            }
        )

}
