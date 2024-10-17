package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.repository.KakaoAuthenticationRepository
import com.kappzzang.jeongsan.repository.UserInfoRepository
import com.kappzzang.jeongsan.repositoryimpl.AuthenticationRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.KakaoAuthenticationRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.UserInfoRepositoryImpl
import com.kappzzang.jeongsan.util.AuthenticationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindUserInfoRepository(
        userInfoRepositoryImpl: UserInfoRepositoryImpl
    ): UserInfoRepository

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
