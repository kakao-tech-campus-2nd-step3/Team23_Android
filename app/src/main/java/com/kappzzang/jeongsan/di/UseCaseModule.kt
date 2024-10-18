package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.repository.ExpenseDetailRepository
import com.kappzzang.jeongsan.repository.ExpenseRepository
import com.kappzzang.jeongsan.repository.GroupInfoRepository
import com.kappzzang.jeongsan.repository.InviteRepository
import com.kappzzang.jeongsan.repository.KakaoAuthenticationRepository
import com.kappzzang.jeongsan.repository.MemberRepository
import com.kappzzang.jeongsan.repository.ReceiptRepository
import com.kappzzang.jeongsan.repository.TransferRepository
import com.kappzzang.jeongsan.repository.UserInfoRepository
import com.kappzzang.jeongsan.usecase.AuthenticateWithKakaoUseCase
import com.kappzzang.jeongsan.usecase.AuthorizeWithKakaoUseCase
import com.kappzzang.jeongsan.usecase.EditExpenseDetailUseCase
import com.kappzzang.jeongsan.usecase.GetDoneGroupUseCase
import com.kappzzang.jeongsan.usecase.GetExpenseDetailUseCase
import com.kappzzang.jeongsan.usecase.GetExpenseUseCase
import com.kappzzang.jeongsan.usecase.GetInviteInfoUseCase
import com.kappzzang.jeongsan.usecase.GetProgressingGroupUseCase
import com.kappzzang.jeongsan.usecase.GetTransferInfoUseCase
import com.kappzzang.jeongsan.usecase.GetUserInfoUseCase
import com.kappzzang.jeongsan.usecase.RegisterWithKakaoUseCase
import com.kappzzang.jeongsan.usecase.SendInviteMessageUseCase
import com.kappzzang.jeongsan.usecase.SendTransferMessageUseCase
import com.kappzzang.jeongsan.usecase.UploadExpenseUseCase
import com.kappzzang.jeongsan.usecase.UploadGroupInfoUseCase
import com.kappzzang.jeongsan.util.AuthenticationRepository
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

    @Provides
    fun provideSendInviteMessageUseCase(inviteRepository: InviteRepository) =
        SendInviteMessageUseCase(inviteRepository)
}
