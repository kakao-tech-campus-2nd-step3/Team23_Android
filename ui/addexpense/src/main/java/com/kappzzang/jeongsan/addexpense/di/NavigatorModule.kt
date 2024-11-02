package com.kappzzang.jeongsan.addexpense.di

import com.kappzzang.jeongsan.addexpense.navigation.AddExpenseNavigatorImpl
import com.kappzzang.jeongsan.navigation.AddExpenseNavigator
import dagger.Binds
import dagger.Module

@Module
abstract class NavigatorModule {
    @Binds
    abstract fun bindExpenseListNavigator(appNavigatorImpl: AddExpenseNavigatorImpl): AddExpenseNavigator
}
