package com.kappzzang.jeongsan.expensedetail.di

import com.kappzzang.jeongsan.expensedetail.navigation.ExpenseDetailNavigatorImpl
import com.kappzzang.jeongsan.navigation.ExpenseDetailNavigator
import dagger.Binds
import dagger.Module

@Module
abstract class NavigatorModule {
    @Binds
    abstract fun bindExpenseDetailNavigator(appNavigatorImpl: ExpenseDetailNavigatorImpl): ExpenseDetailNavigator
}
