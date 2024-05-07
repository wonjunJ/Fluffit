package com.kiwa.data.repository

import com.kiwa.data.datasource.MainDataSource
import com.kiwa.domain.repository.MainRepository
import com.kiwa.fluffit.model.main.MainUIModel
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val mainDataSource: MainDataSource) :
    MainRepository {
    override suspend fun getMainUIInfo(): Result<MainUIModel> =
        mainDataSource.fetchMainUIInfo().fold(
            onSuccess = { Result.success(it.data) },
            onFailure = { Result.failure(it) }
        )

}
