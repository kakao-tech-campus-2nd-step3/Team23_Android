package com.kappzzang.jeongsan.login.di

import com.kappzzang.jeongsan.login.navigation.LoginNavigatorImpl
import com.kappzzang.jeongsan.navigation.LoginNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigatorModule {
    @Binds
    abstract fun bindLoginNavigator(appNavigatorImpl: LoginNavigatorImpl): LoginNavigator
}
