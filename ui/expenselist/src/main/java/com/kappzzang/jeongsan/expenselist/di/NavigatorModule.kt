package com.kappzzang.jeongsan.expenselist.di

import com.kappzzang.jeongsan.expenselist.navigation.ExpenseListNavigatorImpl
import com.kappzzang.jeongsan.navigation.ExpenseListNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigatorModule {
    @Binds
    abstract fun bindExpenseListNavigator(appNavigatorImpl: ExpenseListNavigatorImpl): ExpenseListNavigator
}
