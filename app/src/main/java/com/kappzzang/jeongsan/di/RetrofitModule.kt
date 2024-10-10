package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.api.KakaoAuthRetrofitService
import com.kappzzang.jeongsan.retrofit.RetrofitModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    fun provideKakaoAuthRetrofitService (@RetrofitModule.KakaoAuthRetrofit kakaoAuthRetrofit: Retrofit): KakaoAuthRetrofitService =
        kakaoAuthRetrofit.create(KakaoAuthRetrofitService::class.java)
}
