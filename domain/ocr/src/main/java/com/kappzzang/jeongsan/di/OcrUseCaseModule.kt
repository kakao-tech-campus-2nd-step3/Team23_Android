package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.repository.ReceiptCaptureRepository
import com.kappzzang.jeongsan.usecase.AnalyzeReceiptImageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object OcrUseCaseModule {

    @Provides
    fun provideAnalyzeReceiptImageUseCase(receiptCaptureRepository: ReceiptCaptureRepository) =
        AnalyzeReceiptImageUseCase(receiptCaptureRepository)
}
