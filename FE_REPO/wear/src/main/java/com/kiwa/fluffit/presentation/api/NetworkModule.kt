package com.kiwa.fluffit.presentation.api

import android.content.SharedPreferences
import com.kiwa.fluffit.BuildConfig
import com.kiwa.fluffit.presentation.token.TokenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.HEADERS)
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BattleSSEClient

    @Singleton
    @Provides
    @BattleSSEClient
    fun provideSSEClient(
//        authAuthenticator: AuthAuthenticator
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.apply {
            readTimeout(60, TimeUnit.SECONDS)
            connectTimeout(60, TimeUnit.SECONDS)
            addInterceptor(loggingInterceptor)
//            authenticator(authAuthenticator)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(sharedPreferences: SharedPreferences): AuthInterceptor {
        return AuthInterceptor(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideBattleService(
        retrofit: Retrofit,
    ): BattleService = retrofit.create((BattleService::class.java))

    @Singleton
    @Provides
    fun provideMatchingService(
        @BattleSSEClient
        okHttpClient: OkHttpClient,
        tokenRepository: TokenRepository
    ): MatchingService =
        MatchingServiceImpl(okHttpClient, tokenRepository)

    @Singleton
    @Provides
    fun provideBattleResultService(
        @BattleSSEClient
        okHttpClient: OkHttpClient,
        tokenRepository: TokenRepository
    ): BattleResultService =
        BattleResultServiceImpl(okHttpClient, tokenRepository)

    @Singleton
    @Provides
    fun provideFeedService(
        retrofit:Retrofit,
    ) : FeedService = retrofit.create(FeedService::class.java)
}
