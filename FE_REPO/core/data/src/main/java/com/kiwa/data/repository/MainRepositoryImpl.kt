package com.kiwa.data.repository

import android.util.Log
import com.kiwa.data.datasource.MainDataSource
import com.kiwa.domain.repository.MainRepository
import com.kiwa.fluffit.model.main.FullnessUpdateInfo
import com.kiwa.fluffit.model.main.MainUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val mainDataSource: MainDataSource) :
    MainRepository {
    private var backup = 100
    private val fullness: Int
        get() {
            backup -= 20
            return backup
        }

    override suspend fun getMainUIInfo(): Result<MainUIModel> =
        mainDataSource.fetchMainUIInfo().fold(
            onSuccess = { Result.success(it.data) },
            onFailure = { Result.failure(it) }
        )

    override fun updateFullness(): Flow<FullnessUpdateInfo> = flow {
        Log.d("확인", "업데이트 호출")
        emit(
            FullnessUpdateInfo(
                fullness = fullness,
                nextUpdateTime = System.currentTimeMillis() + 3000,
                isEvolutionAvailable = true
            )
        )
    }

}
