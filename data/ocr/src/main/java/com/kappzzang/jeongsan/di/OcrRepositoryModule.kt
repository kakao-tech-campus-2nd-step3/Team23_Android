package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.repository.ReceiptCaptureRepository
import com.kappzzang.jeongsan.repositoryimpl.ReceiptCaptureRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OcrRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindReceiptCaptureRepository(
        receiptCaptureRepository: ReceiptCaptureRepositoryImpl
    ): ReceiptCaptureRepository
}
