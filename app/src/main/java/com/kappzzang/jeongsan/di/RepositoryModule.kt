package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.data.repositoryimpl.GroupInfoRepositoryImpl
import com.kappzzang.jeongsan.domain.repository.GroupInfoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGroupInfoRepository(): GroupInfoRepository = GroupInfoRepositoryImpl()
}
