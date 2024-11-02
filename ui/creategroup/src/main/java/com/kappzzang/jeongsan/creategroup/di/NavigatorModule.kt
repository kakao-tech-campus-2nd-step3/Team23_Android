package com.kappzzang.jeongsan.creategroup.di

import com.kappzzang.jeongsan.creategroup.navigation.CreateGroupNavigatorImpl
import com.kappzzang.jeongsan.navigation.CreateGroupNavigator
import dagger.Binds
import dagger.Module

@Module
abstract class NavigatorModule {
    @Binds
    abstract fun bindCreateGroupNavigator(appNavigatorImpl: CreateGroupNavigatorImpl): CreateGroupNavigator
}
