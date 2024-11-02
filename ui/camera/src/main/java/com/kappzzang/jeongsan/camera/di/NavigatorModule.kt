package com.kappzzang.jeongsan.camera.di

import com.kappzzang.jeongsan.camera.navigation.CameraNavigatorImpl
import com.kappzzang.jeongsan.navigation.CameraNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigatorModule {
    @Binds
    abstract fun bindCameraNavigator(appNavigatorImpl: CameraNavigatorImpl): CameraNavigator
}
