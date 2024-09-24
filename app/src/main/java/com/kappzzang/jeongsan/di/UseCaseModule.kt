package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.domain.repository.GroupInfoRepository
import com.kappzzang.jeongsan.domain.repository.UserInfoRepository
import com.kappzzang.jeongsan.domain.usecase.GetDoneGroupUseCase
import com.kappzzang.jeongsan.domain.usecase.GetProgressingGroupUseCase
import com.kappzzang.jeongsan.domain.usecase.GetUserInfoUseCase
import com.kappzzang.jeongsan.domain.repository.MemberRepository
import com.kappzzang.jeongsan.domain.usecase.GetInviteInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetProgressingGroupUseCase(groupInfoRepository: GroupInfoRepository) =
        GetProgressingGroupUseCase(groupInfoRepository)

    @Provides
    fun provideGetDoneGroupUseCase(groupInfoRepository: GroupInfoRepository) =
        GetDoneGroupUseCase(groupInfoRepository)

    @Provides
    fun provideGetUserInfoUseCase(userInfoRepository: UserInfoRepository) =
        GetUserInfoUseCase(userInfoRepository)

    @Provides
    fun provideGetInviteInfoUseCase(memberRepository: MemberRepository): GetInviteInfoUseCase =
        GetInviteInfoUseCase(memberRepository)
}
