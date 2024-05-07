package com.kiwa.data.di

import com.kiwa.data.repository.UserRepositoryImpl
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
}
