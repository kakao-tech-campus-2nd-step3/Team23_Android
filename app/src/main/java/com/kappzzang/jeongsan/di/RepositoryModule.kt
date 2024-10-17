package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.repository.GroupInfoRepository
import com.kappzzang.jeongsan.repository.KakaoAuthenticationRepository
import com.kappzzang.jeongsan.repository.MemberRepository
import com.kappzzang.jeongsan.repository.UserInfoRepository
import com.kappzzang.jeongsan.repositoryimpl.AuthenticationRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.GroupInfoRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.KakaoAuthenticationRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.MemberRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.UserInfoRepositoryImpl
import com.kappzzang.jeongsan.util.AuthenticationRepository
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
    abstract fun bindUserInfoRepository(
        userInfoRepositoryImpl: UserInfoRepositoryImpl
    ): UserInfoRepository

    @Binds
    @Singleton
    abstract fun bindMemberRepository(memberRepositoryImpl: MemberRepositoryImpl): MemberRepository

    @Binds
    @Singleton
    abstract fun bindKakaoAuthenticationRepository(
        kakaoAuthenticationRepositoryImpl: KakaoAuthenticationRepositoryImpl
    ): KakaoAuthenticationRepository

    @Binds
    @Singleton
    abstract fun bindAuthenticationRepository(
        authenticationRepositoryImpl: AuthenticationRepositoryImpl
    ): AuthenticationRepository
}
