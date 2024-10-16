package com.kappzzang.jeongsan.datasource.member

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kappzzang.jeongsan.entity.MemberEntity

@Database(entities = [MemberEntity::class], version = 1)
abstract class MemberDatabase : RoomDatabase() {
    abstract fun getMemberDao(): MemberDao
}
