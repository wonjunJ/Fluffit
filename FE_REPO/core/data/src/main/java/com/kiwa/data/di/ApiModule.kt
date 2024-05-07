package com.kiwa.data.di

import com.kiwa.data.api.AuthService
import com.kiwa.data.api.CollectionService
import com.kiwa.data.api.NaverAuthService
import com.kiwa.data.api.NaverLoginService
import com.kiwa.fluffit.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

private const val NAVER_LOGIN_BASE_URL = "https://openapi.naver.com/v1/"
private const val NAVER_AUTH_URL = "https://nid.naver.com/oauth2.0/"

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class FlupetRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NaverRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NaverAuthRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthRetrofit

    @Singleton
    @Provides
    @FlupetRetrofit
    fun provideCollectionRetrofit(
        @NetworkModule.FlupetClient
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @Singleton
    @Provides
    @NaverRetrofit
    fun provideNaverRetrofit(
        @NetworkModule.SocialLoginClient
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(NAVER_LOGIN_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @Singleton
    @Provides
    @AuthRetrofit
    fun provideAuthRetrofit(
        @NetworkModule.AuthClient
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @Singleton
    @Provides
    @NaverAuthRetrofit
    fun provideNaverAuthRetrofit(
        @NetworkModule.SocialLoginClient
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(NAVER_AUTH_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @Singleton
    @Provides
    fun provideAuthApi(
        @AuthRetrofit
        retrofit: Retrofit
    ): AuthService = retrofit.create((AuthService::class.java))

    @Singleton
    @Provides
    fun provideCollectionApi(
        @FlupetRetrofit
        retrofit: Retrofit
    ): CollectionService = retrofit.create(CollectionService::class.java)

    @Singleton
    @Provides
    fun provideNaverLoginApi(
        @NaverRetrofit
        retrofit: Retrofit
    ): NaverLoginService = retrofit.create((NaverLoginService::class.java))

    @Singleton
    @Provides
    fun provideNaverAuthService(
        @NaverAuthRetrofit
        retrofit: Retrofit
    ): NaverAuthService = retrofit.create((NaverAuthService::class.java))
}
