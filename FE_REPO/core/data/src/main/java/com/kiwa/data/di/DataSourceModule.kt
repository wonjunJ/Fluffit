package com.kiwa.data.di

import com.kiwa.data.datasource.CollectionDataSource
import com.kiwa.data.datasource.CollectionDataSourceImpl
import com.kiwa.data.datasource.FlupetDataSource
import com.kiwa.data.datasource.FlupetDataSourceImpl
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
    abstract fun bindCollectionDataSource(collectionDataSourceImpl: CollectionDataSourceImpl):
        CollectionDataSource

    @Binds
    @Singleton
    abstract fun bindRankingDataSource(rankingDataSourceImpl: RankingDataSourceImpl):
        RankingDataSource

    @Binds
    @Singleton
    abstract fun bindMainDataSource(mainDataSourceImpl: FlupetDataSourceImpl):
        FlupetDataSource
}
