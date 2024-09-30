package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.repository.ExpenseDetailRepository
import com.kappzzang.jeongsan.repository.ExpenseRepository
import com.kappzzang.jeongsan.repository.GroupInfoRepository
import com.kappzzang.jeongsan.repository.MemberRepository
import com.kappzzang.jeongsan.repository.ReceiptRepository
import com.kappzzang.jeongsan.repository.UserInfoRepository
import com.kappzzang.jeongsan.usecase.EditExpenseDetailUseCase
import com.kappzzang.jeongsan.usecase.GetDoneGroupUseCase
import com.kappzzang.jeongsan.usecase.GetExpenseDetailUseCase
import com.kappzzang.jeongsan.usecase.GetExpenseUseCase
import com.kappzzang.jeongsan.usecase.GetInviteInfoUseCase
import com.kappzzang.jeongsan.usecase.GetProgressingGroupUseCase
import com.kappzzang.jeongsan.usecase.GetUserInfoUseCase
import com.kappzzang.jeongsan.usecase.UploadExpenseUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetProgressingGroupUseCase(groupInfoRepository: com.kappzzang.jeongsan.repository.GroupInfoRepository) =
        com.kappzzang.jeongsan.usecase.GetProgressingGroupUseCase(groupInfoRepository)

    @Provides
    fun provideGetDoneGroupUseCase(groupInfoRepository: com.kappzzang.jeongsan.repository.GroupInfoRepository) =
        com.kappzzang.jeongsan.usecase.GetDoneGroupUseCase(groupInfoRepository)

    @Provides
    fun provideGetUserInfoUseCase(userInfoRepository: com.kappzzang.jeongsan.repository.UserInfoRepository) =
        com.kappzzang.jeongsan.usecase.GetUserInfoUseCase(userInfoRepository)

    @Provides
    fun provideGetInviteInfoUseCase(memberRepository: com.kappzzang.jeongsan.repository.MemberRepository): com.kappzzang.jeongsan.usecase.GetInviteInfoUseCase =
        com.kappzzang.jeongsan.usecase.GetInviteInfoUseCase(memberRepository)

    @Provides
    fun provideGetExpenseDetailUseCase(expenseDetailRepository: com.kappzzang.jeongsan.repository.ExpenseDetailRepository) =
        com.kappzzang.jeongsan.usecase.GetExpenseDetailUseCase(expenseDetailRepository)

    @Provides
    fun provideGetExpenseUseCase(expenseRepository: com.kappzzang.jeongsan.repository.ExpenseRepository) =
        com.kappzzang.jeongsan.usecase.GetExpenseUseCase(expenseRepository)

    @Provides
    fun provideEditExpenseDetailUseCase(expenseDetailRepository: com.kappzzang.jeongsan.repository.ExpenseDetailRepository) =
        com.kappzzang.jeongsan.usecase.EditExpenseDetailUseCase(expenseDetailRepository)

    @Provides
    fun provideUploadExpenseUseCase(receiptRepository: com.kappzzang.jeongsan.repository.ReceiptRepository) =
        com.kappzzang.jeongsan.usecase.UploadExpenseUseCase(receiptRepository)
}
