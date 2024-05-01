package com.kiwa.data.di

import androidx.media3.ui.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val logginInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        if(BuildConfig.DEBUG){
            setLevel(HttpLoggingInterceptor.Level.HEADERS)
        }
        else{
            setLevel(HttpLoggingInterceptor.Level.NONE)
        }
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class SocialLoginClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthClient


    @Singleton
    @Provides
    @AuthClient
    fun provideAuthClient(): OkHttpClient{
        val builder = OkHttpClient.Builder()
        builder.apply {
            addInterceptor(logginInterceptor)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    @SocialLoginClient
    fun provideSocialClient(): OkHttpClient{
        val builder = OkHttpClient.Builder()
        builder.apply {
            addInterceptor(logginInterceptor)
        }
        return builder.build()
    }

    private val gson: Gson = GsonBuilder().disableHtmlEscaping().create()
    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }
}
