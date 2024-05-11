package com.kiwa.data.repository

import android.util.Log
import com.kiwa.data.datasource.MainDataSource
import com.kiwa.domain.repository.MainRepository
import com.kiwa.fluffit.model.main.FullnessUpdateInfo
import com.kiwa.fluffit.model.main.HealthUpdateInfo
import com.kiwa.fluffit.model.main.MainUIModel
import com.kiwa.fluffit.model.main.response.toMainUIModel
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val mainDataSource: MainDataSource) :
    MainRepository {

    override suspend fun getMainUIInfo(): Result<MainUIModel> =
        mainDataSource.fetchMainUIInfo().fold(
            onSuccess = {
                Log.d("확인", it.toString())
                Result.success(it.toMainUIModel())
            },
            onFailure = { Result.failure(it) }
        )

    override suspend fun updateFullness(): Result<FullnessUpdateInfo> =
        mainDataSource.fetchFullness().fold(
            onSuccess = { Result.success(it) },
            onFailure = { Result.failure(it) }
        )

    override suspend fun updateHealth(): Result<HealthUpdateInfo> =
        mainDataSource.fetchHealth().fold(
            onSuccess = { Result.success(it) },
            onFailure = { Result.failure(it) }
        )
}
