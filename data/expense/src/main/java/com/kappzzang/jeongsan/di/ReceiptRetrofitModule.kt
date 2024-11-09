package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.api.ReceiptRetrofitService
import com.kappzzang.jeongsan.datasource.ExpenseDetailRemoteDatasource
import com.kappzzang.jeongsan.datasource.ExpenseListRemoteDatasource
import com.kappzzang.jeongsan.retrofit.RetrofitModule.ServiceRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReceiptRetrofitModule {

    @Provides
    @Singleton
    fun provideExpenseListRemoteDataSource(receiptApi: ReceiptRetrofitService) =
        ExpenseListRemoteDatasource(receiptApi)

    @Provides
    @Singleton
    fun provideExpenseDetailRemoteDataSource(receiptApi: ReceiptRetrofitService) =
        ExpenseDetailRemoteDatasource(receiptApi)

    @Provides
    @Singleton
    fun provideGroupRetrofitService(
        @ServiceRetrofit serviceRetrofit: Retrofit
    ): ReceiptRetrofitService = serviceRetrofit
        .create(ReceiptRetrofitService::class.java)
}
