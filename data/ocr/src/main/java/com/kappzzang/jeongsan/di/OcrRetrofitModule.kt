package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.api.OcrRetrofitService
import com.kappzzang.jeongsan.datasource.ReceiptCaptureRemoteDatasource
import com.kappzzang.jeongsan.retrofit.RetrofitModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object OcrRetrofitModule {
    @Provides
    @Singleton
    fun provideOcrRemoteDataSource(groupApi: OcrRetrofitService) =
        ReceiptCaptureRemoteDatasource(groupApi)

    @Provides
    @Singleton
    fun provideGroupRetrofitService(
        @RetrofitModule.ServiceRetrofit serviceRetrofit: Retrofit
    ): OcrRetrofitService = serviceRetrofit
        .create(OcrRetrofitService::class.java)
}
