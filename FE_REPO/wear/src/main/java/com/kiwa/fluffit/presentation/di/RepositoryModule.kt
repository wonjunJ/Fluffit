package com.kiwa.fluffit.presentation.di

import com.kiwa.fluffit.presentation.battle.BattleRepository
import com.kiwa.fluffit.presentation.battle.BattleRepositoryImpl
import com.kiwa.fluffit.presentation.feed.FeedRepository
import com.kiwa.fluffit.presentation.feed.FeedRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindBattleRepository(
        battleRepositoryImpl: BattleRepositoryImpl,
    ): BattleRepository

    @Binds
    @Singleton
    abstract fun bindBFeedRepository(
        feedRepositoryImpl: FeedRepositoryImpl
    ): FeedRepository
}
