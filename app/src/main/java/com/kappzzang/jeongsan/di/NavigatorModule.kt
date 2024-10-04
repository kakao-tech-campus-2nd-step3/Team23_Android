package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.navigation.AppNavigator
import com.kappzzang.jeongsan.navigation.NavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigatorModule {
    @Provides
    @Singleton
    fun provideAppNavigator(appNavigator: NavigatorImpl): AppNavigator = appNavigator
}
