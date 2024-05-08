package com.kiwa.domain.repository

import com.kiwa.fluffit.model.main.FullnessUpdateInfo
import com.kiwa.fluffit.model.main.MainUIModel
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun getMainUIInfo(): Result<MainUIModel>

    fun updateFullness(): Flow<FullnessUpdateInfo>
}
