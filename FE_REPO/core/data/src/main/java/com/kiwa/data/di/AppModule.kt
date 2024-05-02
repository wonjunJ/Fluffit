package com.kiwa.data.di

import android.content.SharedPreferences
import com.kiwa.data.util.TokenManagerImpl
import com.kiwa.domain.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideTokenManager(
        sharedPreferences: SharedPreferences
    ): TokenManager = TokenManagerImpl(sharedPreferences)
}