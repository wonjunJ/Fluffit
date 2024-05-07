package com.kiwa.data.di

import com.kiwa.data.datasource.RankingDataSource
import com.kiwa.data.datasource.RankingDataSourceImpl
import com.kiwa.data.datasource.UserDataSource
import com.kiwa.data.datasource.UserDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindUserDataSource(userDataSourceImpl: UserDataSourceImpl): UserDataSource

    @Binds
    @Singleton
    abstract fun bindRankingDataSource(rankingDataSourceImpl: RankingDataSourceImpl):
        RankingDataSource
}
