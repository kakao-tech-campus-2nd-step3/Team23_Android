package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.repository.ReceiptCaptureRepository
import com.kappzzang.jeongsan.repositoryimpl.ReceiptCaptureRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ReceiptCaptureRepositoryModule {
    @Binds
    @ViewModelScoped
    abstract fun bindReceiptCaptureRepository(
        receiptCaptureRepository: ReceiptCaptureRepositoryImpl
    ): ReceiptCaptureRepository
}
