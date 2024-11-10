package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.repository.ExpenseMessageRepository
import com.kappzzang.jeongsan.repository.GroupInfoRepository
import com.kappzzang.jeongsan.repository.InviteRepository
import com.kappzzang.jeongsan.repository.MemberRepository
import com.kappzzang.jeongsan.repository.UserInfoRepository
import com.kappzzang.jeongsan.usecase.GetDoneGroupUseCase
import com.kappzzang.jeongsan.usecase.GetGroupMemberServiceIdUseCase
import com.kappzzang.jeongsan.usecase.GetInviteInfoUseCase
import com.kappzzang.jeongsan.usecase.GetProgressingGroupUseCase
import com.kappzzang.jeongsan.usecase.SendInviteMessageUseCase
import com.kappzzang.jeongsan.usecase.SendNewExpenseMessageUseCase
import com.kappzzang.jeongsan.usecase.UploadGroupInfoUseCase
import com.kappzzang.jeongsan.util.AuthenticationRepository
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
    fun provideUploadGroupInfoUseCase(
        groupInfoRepository: GroupInfoRepository,
        authenticationRepository: AuthenticationRepository
    ) = UploadGroupInfoUseCase(groupInfoRepository, authenticationRepository)

    @Provides
    fun provideSendInviteMessageUseCase(inviteRepository: InviteRepository) =
        SendInviteMessageUseCase(inviteRepository)

    @Provides
    fun provideGetGroupMemberServiceIdUseCase(
        groupInfoRepository: GroupInfoRepository,
        authenticationRepository: AuthenticationRepository
    ) = GetGroupMemberServiceIdUseCase(groupInfoRepository, authenticationRepository)

    @Provides
    fun provideSendNewExpenseMessageUseCase(
        expenseMessageRepository: ExpenseMessageRepository,
        userInfoRepository: UserInfoRepository
    ) = SendNewExpenseMessageUseCase(expenseMessageRepository, userInfoRepository)
}
