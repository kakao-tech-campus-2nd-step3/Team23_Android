package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.repository.ExpenseDetailRepository
import com.kappzzang.jeongsan.repository.ExpenseListRepository
import com.kappzzang.jeongsan.repository.ExpenseRepository
import com.kappzzang.jeongsan.repository.GroupInfoRepository
import com.kappzzang.jeongsan.repository.KakaoAuthenticationRepository
import com.kappzzang.jeongsan.repository.MemberRepository
import com.kappzzang.jeongsan.repository.ReceiptRepository
import com.kappzzang.jeongsan.repository.TransferRepository
import com.kappzzang.jeongsan.repository.UserInfoRepository
import com.kappzzang.jeongsan.repositoryimpl.AuthenticationRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.ExpenseDetailRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.ExpenseListFakeRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.ExpenseRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.GroupInfoRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.KakaoAuthenticationRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.MemberRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.ReceiptRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.TransferRepositoryImpl
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
