package com.kiwa.fluffit.presentation.health

import android.content.Context
import com.example.wearapp.presentation.HealthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HealthModule {
    @Provides
    fun provideHealthRepository(@ApplicationContext context: Context): HealthRepository {
        return HealthRepository(context)
    }
}