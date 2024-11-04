package com.kappzzang.jeongsan.sendmessage.di

import com.kappzzang.jeongsan.navigation.SendMessageNavigator
import com.kappzzang.jeongsan.sendmessage.navigation.SendMessageNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigatorModule {
    @Binds
    abstract fun bindSendMessageNavigator(
        appNavigatorImpl: SendMessageNavigatorImpl
    ): SendMessageNavigator
}
