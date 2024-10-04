package com.kappzzang.jeongsan.di

import android.content.Context
import com.kappzzang.jeongsan.navigation.AppNavigator
import com.kappzzang.jeongsan.navigation.NavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigatorModule {

    @Provides
    @Singleton
    fun provideAppNavigator(@ApplicationContext context: Context): AppNavigator =
        NavigatorImpl(context)
}
