package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.api.GroupRetrofitService
import com.kappzzang.jeongsan.datasource.remote.GroupRemoteDataSource
import com.kappzzang.jeongsan.retrofit.RetrofitModule.ServiceRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object GroupRetrofitModule {

    @Provides
    @Singleton
    fun provideGroupRemoteDataSource(groupApi: GroupRetrofitService) =
        GroupRemoteDataSource(groupApi)

    @Provides
    @Singleton
    fun provideGroupRetrofitService(
        @ServiceRetrofit serviceRetrofit: Retrofit
    ): GroupRetrofitService = serviceRetrofit
        .create(GroupRetrofitService::class.java)
}
