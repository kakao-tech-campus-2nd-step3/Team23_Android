package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.domain.repository.ExpenseDetailRepository
import com.kappzzang.jeongsan.domain.repository.ExpenseRepository
import com.kappzzang.jeongsan.domain.repository.GroupInfoRepository
import com.kappzzang.jeongsan.domain.repository.MemberRepository
import com.kappzzang.jeongsan.domain.repository.UserInfoRepository
import com.kappzzang.jeongsan.domain.usecase.EditExpenseDetailUseCase
import com.kappzzang.jeongsan.domain.usecase.GetDoneGroupUseCase
import com.kappzzang.jeongsan.domain.usecase.GetExpenseDetailUseCase
import com.kappzzang.jeongsan.domain.usecase.GetExpenseUseCase
import com.kappzzang.jeongsan.domain.usecase.GetInviteInfoUseCase
import com.kappzzang.jeongsan.domain.usecase.GetProgressingGroupUseCase
import com.kappzzang.jeongsan.domain.usecase.GetUserInfoUseCase
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

    @Provides
    fun provideGetExpenseDetailUseCase(expenseDetailRepository: ExpenseDetailRepository) =
        GetExpenseDetailUseCase(expenseDetailRepository)

    @Provides
    fun provideGetExpenseUseCase(expenseRepository: ExpenseRepository) =
        GetExpenseUseCase(expenseRepository)

    @Provides
    fun provideEditExpenseDetailUseCase(expenseDetailRepository: ExpenseDetailRepository) =
        EditExpenseDetailUseCase(expenseDetailRepository)
}
