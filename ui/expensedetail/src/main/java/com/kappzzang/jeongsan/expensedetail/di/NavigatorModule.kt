package com.kappzzang.jeongsan.expensedetail.di

import com.kappzzang.jeongsan.expensedetail.navigation.ExpenseDetailNavigatorImpl
import com.kappzzang.jeongsan.navigation.ExpenseDetailNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigatorModule {
    @Binds
    abstract fun bindExpenseDetailNavigator(appNavigatorImpl: ExpenseDetailNavigatorImpl): ExpenseDetailNavigator
}
