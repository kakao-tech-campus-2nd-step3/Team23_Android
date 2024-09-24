package com.kappzzang.jeongsan.di

import android.content.Context
import com.kappzzang.jeongsan.data.datasource.expense.ExpenseDatabase
import com.kappzzang.jeongsan.data.datasource.group.GroupDatabase
import androidx.room.Room
import com.kappzzang.jeongsan.data.datasource.MemberContract
import com.kappzzang.jeongsan.data.datasource.MemberDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    // TODO: Dummy 데이터를 넣기 위해 getInstance 함수를 사용함
    @Provides
    @Singleton
    fun provideGroupDatabase(@ApplicationContext context: Context): GroupDatabase =
        GroupDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideExpenseDatabase(@ApplicationContext context: Context): ExpenseDatabase =
        ExpenseDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideMemberDatabase(@ApplicationContext context: Context): MemberDatabase =
        Room.databaseBuilder(
            context,
            MemberDatabase::class.java,
            MemberContract.DATABASE_NAME
        ).build()
}
