package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.repository.ExpenseDetailRepository
import com.kappzzang.jeongsan.repository.ExpenseListRepository
import com.kappzzang.jeongsan.repository.ExpenseRepository
import com.kappzzang.jeongsan.repository.GroupInfoRepository
import com.kappzzang.jeongsan.repository.MemberRepository
import com.kappzzang.jeongsan.repository.ReceiptCaptureRepository
import com.kappzzang.jeongsan.repository.ReceiptRepository
import com.kappzzang.jeongsan.repository.UserInfoRepository
import com.kappzzang.jeongsan.repositoryimpl.ExpenseDetailRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.ExpenseListFakeRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.ExpenseRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.GroupInfoRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.MemberRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.ReceiptCaptureRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.ReceiptRepositoryImpl
import com.kappzzang.jeongsan.repositoryimpl.UserInfoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGroupInfoRepository(
        groupInfoRepositoryImpl: com.kappzzang.jeongsan.repositoryimpl.GroupInfoRepositoryImpl
    ): com.kappzzang.jeongsan.repository.GroupInfoRepository

    @Binds
    @Singleton
    abstract fun bindUserInfoRepository(
        userInfoRepositoryImpl: com.kappzzang.jeongsan.repositoryimpl.UserInfoRepositoryImpl
    ): com.kappzzang.jeongsan.repository.UserInfoRepository

    @Binds
    @Singleton
    abstract fun bindExpensePageRepository(
        expenseListRepository: com.kappzzang.jeongsan.repositoryimpl.ExpenseListFakeRepositoryImpl
    ): com.kappzzang.jeongsan.repository.ExpenseListRepository

    @Binds
    @Singleton
    abstract fun bindMemberRepository(
        memberRepositoryImpl: com.kappzzang.jeongsan.repositoryimpl.MemberRepositoryImpl
    ): com.kappzzang.jeongsan.repository.MemberRepository

    @Binds
    @Singleton
    abstract fun bindExpenseDetailRepository(
        expenseDetailRepositoryImpl:
        com.kappzzang.jeongsan.repositoryimpl.ExpenseDetailRepositoryImpl
    ): com.kappzzang.jeongsan.repository.ExpenseDetailRepository

    @Binds
    @Singleton
    abstract fun bindExpenseRepository(
        expenseRepositoryImpl: com.kappzzang.jeongsan.repositoryimpl.ExpenseRepositoryImpl
    ): com.kappzzang.jeongsan.repository.ExpenseRepository

    @Binds
    @Singleton
    abstract fun bindReceiptRepository(
        receiptRepositoryImpl: com.kappzzang.jeongsan.repositoryimpl.ReceiptRepositoryImpl
    ): com.kappzzang.jeongsan.repository.ReceiptRepository
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelRepositoryModule {
    @Binds
    @ViewModelScoped
    abstract fun bindReceiptCaptureRepository(
        receiptCaptureRepository: com.kappzzang.jeongsan.repositoryimpl.ReceiptCaptureRepositoryImpl
    ): com.kappzzang.jeongsan.repository.ReceiptCaptureRepository
}
