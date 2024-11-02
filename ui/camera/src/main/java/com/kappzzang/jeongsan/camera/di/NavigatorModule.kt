package com.kappzzang.jeongsan.camera.di

import com.kappzzang.jeongsan.camera.navigation.CameraNavigatorImpl
import com.kappzzang.jeongsan.navigation.CameraNavigator
import dagger.Binds
import dagger.Module

@Module
abstract class NavigatorModule {
    @Binds
    abstract fun bindCameraNavigator(appNavigatorImpl: CameraNavigatorImpl): CameraNavigator
}
