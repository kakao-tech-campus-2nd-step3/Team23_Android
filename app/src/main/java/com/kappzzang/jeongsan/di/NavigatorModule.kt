package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.navigation.AppNavigator
import com.kappzzang.jeongsan.navigation.NavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigatorModule {

    @Binds
    @Singleton
    abstract fun bindAppNavigator(appNavigatorImpl: NavigatorImpl): AppNavigator
}
