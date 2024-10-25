package com.kappzzang.jeongsan.di

import android.content.Context
import com.kappzzang.jeongsan.datasource.expense.ExpenseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExpenseRoomModule {
    @Provides
    @Singleton
    fun provideExpenseDatabase(@ApplicationContext context: Context): ExpenseDatabase =
        ExpenseDatabase.getInstance(context)
}
