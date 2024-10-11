package com.kappzzang.jeongsan.retrofit

import com.kappzzang.jeongsan.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KakaoAuthRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KakaoApiRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ServiceRetrofit

    @Provides
    @Singleton
    @KakaoAuthRetrofit
    fun provideKakaoAuthRetrofitBuilder(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.KAKAO_AUTH_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @KakaoApiRetrofit
    fun provideKakaoApiRetrofitBuilder(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.KAKAO_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @ServiceRetrofit
    fun provideServiceRetrofitBuilder(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.SERVICE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
