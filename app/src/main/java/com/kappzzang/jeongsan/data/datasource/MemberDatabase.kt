package com.kappzzang.jeongsan.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kappzzang.jeongsan.data.datasource.dao.MemberDao
import com.kappzzang.jeongsan.data.entity.MemberEntity

@Database(entities = [MemberEntity::class], version = 1)
abstract class MemberDatabase : RoomDatabase() {
    abstract fun getMemberDao(): MemberDao
}