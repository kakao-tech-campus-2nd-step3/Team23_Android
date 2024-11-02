package com.kappzzang.jeongsan.sendmessage.di

import com.kappzzang.jeongsan.navigation.SendMessageNavigator
import com.kappzzang.jeongsan.sendmessage.navigation.SendMessageNavigatorImpl
import dagger.Binds
import dagger.Module

@Module
abstract class NavigatorModule {
    @Binds
    abstract fun bindSendMessageNavigator(appNavigatorImpl: SendMessageNavigatorImpl): SendMessageNavigator
}
