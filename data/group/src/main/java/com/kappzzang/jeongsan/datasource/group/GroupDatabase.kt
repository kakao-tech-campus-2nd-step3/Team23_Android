package com.kappzzang.jeongsan.datasource.group

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kappzzang.jeongsan.entity.GroupEntity

@Database(entities = [GroupEntity::class], version = 1)
abstract class GroupDatabase : RoomDatabase() {

    abstract fun groupDao(): GroupDao

    companion object {

        fun getInstance(context: Context): GroupDatabase = Room
            .databaseBuilder(context, GroupDatabase::class.java, GroupContract.DATABASE_NAME)
            .build()
    }
}
