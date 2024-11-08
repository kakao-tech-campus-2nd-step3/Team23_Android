package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.api.KakaoAuthRetrofitService
import com.kappzzang.jeongsan.api.ServerAuthRetrofitService
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
    fun provideKakaoAuthRetrofitService(
        @RetrofitModule.KakaoAuthRetrofit kakaoAuthRetrofit: Retrofit
    ): KakaoAuthRetrofitService = kakaoAuthRetrofit.create(KakaoAuthRetrofitService::class.java)

    @Provides
    fun provideServerAuthRetrofitService(
        @RetrofitModule.ServiceAuthRetrofit serverAuthRetrofit: Retrofit
    ): ServerAuthRetrofitService = serverAuthRetrofit.create(ServerAuthRetrofitService::class.java)
}
