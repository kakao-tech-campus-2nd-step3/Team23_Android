package com.kappzzang.jeongsan.addexpense.di

import com.kappzzang.jeongsan.addexpense.navigation.AddExpenseNavigatorImpl
import com.kappzzang.jeongsan.navigation.AddExpenseNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigatorModule {
    @Binds
    abstract fun bindExpenseListNavigator(appNavigatorImpl: AddExpenseNavigatorImpl): AddExpenseNavigator
}
