package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.data.repositoryimpl.ExpenseListFakeRepositoryImpl
import com.kappzzang.jeongsan.data.repositoryimpl.GroupInfoRepositoryImpl
import com.kappzzang.jeongsan.domain.repository.ExpenseListRepository
import com.kappzzang.jeongsan.domain.repository.GroupInfoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ExpenseListRepositoryModule {
    @Binds
    @ViewModelScoped
    abstract fun bindExpensePageRepository(
        expenseListRepository: ExpenseListFakeRepositoryImpl
    ): ExpenseListRepository
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class GroupInfoRepositoryModule {
    @Binds
    @ViewModelScoped
    abstract fun bindGroupInfoRepository(
        groupInfoRepository: GroupInfoRepositoryImpl
    ): GroupInfoRepository
}
