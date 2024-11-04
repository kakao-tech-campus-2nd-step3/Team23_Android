package com.kappzzang.jeongsan.main.di

import com.kappzzang.jeongsan.main.navigation.MainPageNavigatorImpl
import com.kappzzang.jeongsan.navigation.MainPageNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigatorModule {
    @Binds
    abstract fun bindMainPageNavigator(appNavigatorImpl: MainPageNavigatorImpl): MainPageNavigator
}
