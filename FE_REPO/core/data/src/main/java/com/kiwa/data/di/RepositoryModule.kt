package com.kiwa.data.di

import com.kiwa.data.repository.BattleRecordRepositoryImpl
import com.kiwa.data.repository.CollectionRepositoryImpl
import com.kiwa.data.repository.FlupetRepositoryImpl
import com.kiwa.data.repository.RankingRepositoryImpl
import com.kiwa.data.repository.UserRepositoryImpl
import com.kiwa.domain.repository.BattleRecordRepository
import com.kiwa.domain.repository.CollectionRepository
import com.kiwa.domain.repository.FlupetRepository
import com.kiwa.domain.repository.RankingRepository
import com.kiwa.domain.repository.UserRepository
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
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindRankingRepository(rankingRepositoryImpl: RankingRepositoryImpl):
        RankingRepository

    @Binds
    @Singleton
    abstract fun bindCollectionRepository(
        collectionRepositoryImpl: CollectionRepositoryImpl
    ): CollectionRepository

    @Binds
    @Singleton
    abstract fun bindMainRepository(
        mainRepositoryImpl: FlupetRepositoryImpl
    ): FlupetRepository

    @Binds
    @Singleton
    abstract fun bindBattleRecordRepository(
        battleRecordRepositoryImpl: BattleRecordRepositoryImpl
    ): BattleRecordRepository
}
