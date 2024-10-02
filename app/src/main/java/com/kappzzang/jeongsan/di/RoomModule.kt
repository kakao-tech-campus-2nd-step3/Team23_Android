package com.kappzzang.jeongsan.di

import android.content.Context
import androidx.room.Room
import com.kappzzang.jeongsan.datasource.expense.ExpenseDatabase
import com.kappzzang.jeongsan.datasource.group.GroupDatabase
import com.kappzzang.jeongsan.datasource.member.MemberContract
import com.kappzzang.jeongsan.datasource.member.MemberDatabase
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
    fun provideGroupDatabase(
        @ApplicationContext context: Context
    ): com.kappzzang.jeongsan.datasource.group.GroupDatabase =
        com.kappzzang.jeongsan.datasource.group.GroupDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideExpenseDatabase(
        @ApplicationContext context: Context
    ): com.kappzzang.jeongsan.datasource.expense.ExpenseDatabase =
        com.kappzzang.jeongsan.datasource.expense.ExpenseDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideMemberDatabase(
        @ApplicationContext context: Context
    ): com.kappzzang.jeongsan.datasource.member.MemberDatabase = Room.databaseBuilder(
        context,
        com.kappzzang.jeongsan.datasource.member.MemberDatabase::class.java,
        com.kappzzang.jeongsan.datasource.member.MemberContract.DATABASE_NAME
    ).build()
}
