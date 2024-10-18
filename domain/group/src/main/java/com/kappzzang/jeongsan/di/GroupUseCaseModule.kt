package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.repository.GroupInfoRepository
import com.kappzzang.jeongsan.repository.InviteRepository
import com.kappzzang.jeongsan.repository.MemberRepository
import com.kappzzang.jeongsan.usecase.GetDoneGroupUseCase
import com.kappzzang.jeongsan.usecase.GetInviteInfoUseCase
import com.kappzzang.jeongsan.usecase.GetProgressingGroupUseCase
import com.kappzzang.jeongsan.usecase.SendInviteMessageUseCase
import com.kappzzang.jeongsan.usecase.UploadGroupInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object GroupUseCaseModule {
    @Provides
    fun provideGetProgressingGroupUseCase(groupInfoRepository: GroupInfoRepository) =
        GetProgressingGroupUseCase(groupInfoRepository)

    @Provides
    fun provideGetDoneGroupUseCase(groupInfoRepository: GroupInfoRepository) =
        GetDoneGroupUseCase(groupInfoRepository)

    @Provides
    fun provideGetInviteInfoUseCase(memberRepository: MemberRepository): GetInviteInfoUseCase =
        GetInviteInfoUseCase(memberRepository)

    @Provides
    fun provideUploadGroupInfoUseCase(groupInfoRepository: GroupInfoRepository) =
        UploadGroupInfoUseCase(groupInfoRepository)

    @Provides
    fun provideSendInviteMessageUseCase(inviteRepository: InviteRepository) =
        SendInviteMessageUseCase(inviteRepository)
}
