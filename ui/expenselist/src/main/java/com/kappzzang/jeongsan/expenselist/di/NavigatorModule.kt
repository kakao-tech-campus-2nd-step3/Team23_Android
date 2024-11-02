package com.kappzzang.jeongsan.expenselist.di

import com.kappzzang.jeongsan.expenselist.navigation.ExpenseListNavigatorImpl
import com.kappzzang.jeongsan.navigation.ExpenseListNavigator
import dagger.Binds
import dagger.Module

@Module
abstract class NavigatorModule {
    @Binds
    abstract fun bindExpenseListNavigator(appNavigatorImpl: ExpenseListNavigatorImpl): ExpenseListNavigator
}
