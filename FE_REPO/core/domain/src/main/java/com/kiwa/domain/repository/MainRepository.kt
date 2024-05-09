package com.kiwa.domain.repository

import com.kiwa.fluffit.model.main.FullnessUpdateInfo
import com.kiwa.fluffit.model.main.HealthUpdateInfo
import com.kiwa.fluffit.model.main.MainUIModel

interface MainRepository {
    suspend fun getMainUIInfo(): Result<MainUIModel>

    fun updateFullness(): Result<FullnessUpdateInfo>
    fun updateHealth(): Result<HealthUpdateInfo>
}
