package com.kiwa.fluffit.presentation.di

import com.kiwa.fluffit.presentation.datasource.BattleDataSource
import com.kiwa.fluffit.presentation.datasource.BattleDataSourceImpl
import com.kiwa.fluffit.presentation.datasource.FeedDataSource
import com.kiwa.fluffit.presentation.datasource.FeedDataSourceImpl
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
    abstract fun bindBattleDataSource(battleDataSourceImpl: BattleDataSourceImpl):
        BattleDataSource

    @Binds
    @Singleton
    abstract fun bindFeedDataSource(feedDataSourceImpl: FeedDataSourceImpl):
        FeedDataSource
}
