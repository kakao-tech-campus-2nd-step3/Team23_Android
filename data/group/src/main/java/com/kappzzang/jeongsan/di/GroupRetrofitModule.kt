package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.api.GroupRetrofitService
import com.kappzzang.jeongsan.build_config.BuildConfig
import com.kappzzang.jeongsan.datasource.remote.GroupRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GroupRetrofitModule {

    @Provides
    @Singleton
    fun provideGroupRemoteDataSource(groupApi: GroupRetrofitService): GroupRemoteDataSource {
        return GroupRemoteDataSource(groupApi)
    }

    @Provides
    @Singleton
    fun provideGroupRetrofitService(): GroupRetrofitService = Retrofit.Builder()
        .baseUrl(BuildConfig.SERVICE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GroupRetrofitService::class.java)
}