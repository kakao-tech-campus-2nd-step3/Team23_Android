package com.kappzzang.jeongsan.di

import javax.inject.Qualifier

object DispatchersAnnotations {
    @Qualifier
    annotation class IoDispatcher

    @Qualifier
    annotation class MainDispatcher
}
