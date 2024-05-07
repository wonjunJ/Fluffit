package com.kiwa.domain.repository

import com.kiwa.fluffit.model.main.MainUIModel

interface MainRepository {
    suspend fun getMainUIInfo(): Result<MainUIModel>
}
