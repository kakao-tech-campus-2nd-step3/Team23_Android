package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.repository.ExpenseDetailRepository
import com.kappzzang.jeongsan.repository.ExpenseRepository
import com.kappzzang.jeongsan.repository.ReceiptRepository
import com.kappzzang.jeongsan.repository.TransferRepository
import com.kappzzang.jeongsan.repositoryimpl.ExpenseDetailRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.ExpenseFakeRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.ReceiptRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.TransferRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ExpenseRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindExpenseRepository(
        expenseListRepository: ExpenseFakeRepositoryImpl
    ): ExpenseRepository

    @Binds
    @Singleton
    abstract fun bindExpenseDetailRepository(
        expenseDetailRepositoryImpl: ExpenseDetailRepositoryImpl
    ): ExpenseDetailRepository

    @Binds
    @Singleton
    abstract fun bindReceiptRepository(
        receiptRepositoryImpl: ReceiptRepositoryImpl
    ): ReceiptRepository

    @Binds
    @Singleton
    abstract fun bindTransferRepository(
        transferRepositoryImpl: TransferRepositoryImpl
    ): TransferRepository
}
