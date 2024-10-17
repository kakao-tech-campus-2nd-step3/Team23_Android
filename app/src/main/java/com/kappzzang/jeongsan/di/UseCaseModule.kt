package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.repository.GroupInfoRepository
import com.kappzzang.jeongsan.repository.MemberRepository
import com.kappzzang.jeongsan.repository.TransferRepository
import com.kappzzang.jeongsan.repository.UserInfoRepository
import com.kappzzang.jeongsan.usecase.GetDoneGroupUseCase
import com.kappzzang.jeongsan.usecase.GetInviteInfoUseCase
import com.kappzzang.jeongsan.usecase.GetProgressingGroupUseCase
import com.kappzzang.jeongsan.usecase.GetTransferInfoUseCase
import com.kappzzang.jeongsan.usecase.SendTransferMessageUseCase
import com.kappzzang.jeongsan.usecase.UploadGroupInfoUseCase
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
    fun provideGetInviteInfoUseCase(memberRepository: MemberRepository): GetInviteInfoUseCase =
        GetInviteInfoUseCase(memberRepository)

    @Provides
    fun provideUploadGroupInfoUseCase(groupInfoRepository: GroupInfoRepository) =
        UploadGroupInfoUseCase(groupInfoRepository)

    @Provides
    fun provideGetTransferInfoUseCase(transferRepository: TransferRepository) =
        GetTransferInfoUseCase(transferRepository)

    @Provides
    fun provideSendTransferMessageUseCase(
        userInfoRepository: UserInfoRepository,
        transferRepository: TransferRepository
    ) = SendTransferMessageUseCase(userInfoRepository, transferRepository)
}
