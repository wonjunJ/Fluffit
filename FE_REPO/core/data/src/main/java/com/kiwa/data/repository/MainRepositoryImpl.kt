package com.kiwa.data.repository

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

    override suspend fun updateFullness(): Result<FullnessUpdateInfo> =
        mainDataSource.fetchFullness().fold(
            onSuccess = {
                Result.success(
                    FullnessUpdateInfo(
                        fullness = fullness,
                        nextUpdateTime = System.currentTimeMillis() + 3000,
                        isEvolutionAvailable = true
                    )
                )
//                Result.success(it.data)
            },
            onFailure = { Result.failure(it) }
        )

    override suspend fun updateHealth(): Result<HealthUpdateInfo> =
        mainDataSource.fetchHealth().fold(
            onSuccess = {
                Result.success(
                    HealthUpdateInfo(
                        health = health,
                        nextUpdateTime = System.currentTimeMillis() + 3000,
                        isEvolutionAvailable = true
                    )
                )
//                Result.success(it.data)
            },
            onFailure = { Result.failure(it) }
        )
}
