package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.repository.ExpenseDetailRepository
import com.kappzzang.jeongsan.repository.ExpenseRepository
import com.kappzzang.jeongsan.repository.TransferRepository
import com.kappzzang.jeongsan.repository.UserInfoRepository
import com.kappzzang.jeongsan.usecase.EditExpenseDetailUseCase
import com.kappzzang.jeongsan.usecase.GetExpenseDetailUseCase
import com.kappzzang.jeongsan.usecase.GetTransferInfoUseCase
import com.kappzzang.jeongsan.usecase.SendTransferMessageUseCase
import com.kappzzang.jeongsan.usecase.UploadExpenseUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ExpenseUseCaseModule {
    @Provides
    fun provideGetExpenseDetailUseCase(expenseDetailRepository: ExpenseDetailRepository) =
        GetExpenseDetailUseCase(expenseDetailRepository)

    @Provides
    fun provideEditExpenseDetailUseCase(expenseDetailRepository: ExpenseDetailRepository) =
        EditExpenseDetailUseCase(expenseDetailRepository)

    @Provides
    fun provideUploadExpenseUseCase(expenseRepository: ExpenseRepository) =
        UploadExpenseUseCase(expenseRepository)

    @Provides
    fun provideGetTransferInfoUseCase(transferRepository: TransferRepository) =
        GetTransferInfoUseCase(transferRepository)

    @Provides
    fun provideSendTransferMessageUseCase(
        userInfoRepository: UserInfoRepository,
        transferRepository: TransferRepository
    ) = SendTransferMessageUseCase(userInfoRepository, transferRepository)
}
