package com.kappzzang.jeongsan.main.di

import com.kappzzang.jeongsan.main.navigation.MainPageNavigatorImpl
import com.kappzzang.jeongsan.navigation.LoginNavigator
import dagger.Binds
import dagger.Module

@Module
abstract class NavigatorModule {
    @Binds
    abstract fun bindMainPageNavigator(appNavigatorImpl: MainPageNavigatorImpl): LoginNavigator
}
