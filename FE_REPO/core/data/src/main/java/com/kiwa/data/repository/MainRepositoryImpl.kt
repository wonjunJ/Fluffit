package com.kiwa.data.repository

import android.util.Log
import com.kiwa.data.datasource.MainDataSource
import com.kiwa.domain.repository.MainRepository
import com.kiwa.fluffit.model.main.FullnessUpdateInfo
import com.kiwa.fluffit.model.main.HealthUpdateInfo
import com.kiwa.fluffit.model.main.MainUIModel
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val mainDataSource: MainDataSource) :
    MainRepository {
    private var healthBackup = 100
    private var fullnessBackup = 100
    private val fullness: Int
        get() {
            fullnessBackup -= 20
            if (fullnessBackup < 0) {
                fullnessBackup = 100
            }
            return fullnessBackup
        }

    private val health: Int
        get() {
            healthBackup -= 20
            if (healthBackup < 0) {
                healthBackup = 100
            }
            return healthBackup
        }

    override suspend fun getMainUIInfo(): Result<MainUIModel> =
        mainDataSource.fetchMainUIInfo().fold(
            onSuccess = { Result.success(it.data) },
            onFailure = { Result.failure(it) }
        )

    override fun updateFullness(): Result<FullnessUpdateInfo> {
        return Result.success(
            FullnessUpdateInfo(
                fullness = fullness,
                nextUpdateTime = System.currentTimeMillis() + 3000,
                isEvolutionAvailable = true
            )
        )
    }

    override fun updateHealth(): Result<HealthUpdateInfo> {
        Log.d("확인", "updateHealth")
        return Result.success(
            HealthUpdateInfo(
                health = health,
                nextUpdateTime = System.currentTimeMillis() + 3000,
                isEvolutionAvailable = true
            )
        )
    }
}
