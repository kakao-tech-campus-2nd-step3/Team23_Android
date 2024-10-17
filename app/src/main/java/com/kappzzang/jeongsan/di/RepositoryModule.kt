package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.repository.GroupInfoRepository
import com.kappzzang.jeongsan.repository.MemberRepository
import com.kappzzang.jeongsan.repositoryimpl.GroupInfoRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.MemberRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGroupInfoRepository(
        groupInfoRepositoryImpl: GroupInfoRepositoryImpl
    ): GroupInfoRepository

    @Binds
    @Singleton
    abstract fun bindMemberRepository(memberRepositoryImpl: MemberRepositoryImpl): MemberRepository
}
