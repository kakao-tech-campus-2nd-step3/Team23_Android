package com.kappzzang.jeongsan.di

import android.content.Context
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

    @Provides
    @Singleton
    fun provideMemberDatabase(@ApplicationContext context: Context): MemberDatabase = Room.databaseBuilder(
            context,
            MemberDatabase::class.java,
            MemberContract.DATABASE_NAME
        ).build()
}
