package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.data.repositoryimpl.ExpenseListFakeRepositoryImpl
import com.kappzzang.jeongsan.data.repositoryimpl.GroupInfoRepositoryImpl
import com.kappzzang.jeongsan.data.repositoryimpl.MemberRepositoryImpl
import com.kappzzang.jeongsan.data.repositoryimpl.ReceiptRepositoryImpl
import com.kappzzang.jeongsan.data.repositoryimpl.UserInfoRepositoryImpl
import com.kappzzang.jeongsan.domain.repository.ExpenseListRepository
import com.kappzzang.jeongsan.domain.repository.GroupInfoRepository
import com.kappzzang.jeongsan.domain.repository.MemberRepository
import com.kappzzang.jeongsan.domain.repository.ReceiptRepository
import com.kappzzang.jeongsan.domain.repository.UserInfoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGroupInfoRepository(
        groupInfoRepositoryImpl: GroupInfoRepositoryImpl
    ): GroupInfoRepository

    @Binds
    @Singleton
    abstract fun bindUserInfoRepository(
        userInfoRepositoryImpl: UserInfoRepositoryImpl
    ): UserInfoRepository

    @Binds
    @Singleton
    abstract fun bindExpensePageRepository(
        expenseListRepository: ExpenseListFakeRepositoryImpl
    ): ExpenseListRepository

    @Binds
    @Singleton
    abstract fun bindMemberRepository(memberRepositoryImpl: MemberRepositoryImpl): MemberRepository

    @Binds
    @Singleton
    abstract fun bindReceiptRepository(
        receiptRepositoryImpl: ReceiptRepositoryImpl
    ): ReceiptRepository
}
