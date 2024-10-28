package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.repository.ExpenseDetailRepository
import com.kappzzang.jeongsan.repository.ExpenseRepository
import com.kappzzang.jeongsan.repository.ReceiptRepository
import com.kappzzang.jeongsan.usecase.EditExpenseDetailUseCase
import com.kappzzang.jeongsan.usecase.GetExpenseDetailUseCase
import com.kappzzang.jeongsan.usecase.GetExpenseUseCase
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
    fun provideGetExpenseUseCase(expenseRepository: ExpenseRepository) =
        GetExpenseUseCase(expenseRepository)

    @Provides
    fun provideEditExpenseDetailUseCase(expenseDetailRepository: ExpenseDetailRepository) =
        EditExpenseDetailUseCase(expenseDetailRepository)

    @Provides
    fun provideUploadExpenseUseCase(receiptRepository: ReceiptRepository) =
        UploadExpenseUseCase(receiptRepository)
}
