package com.kappzzang.jeongsan.datasource.group

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kappzzang.jeongsan.entity.GroupEntity
import java.util.concurrent.Executors
import kotlinx.coroutines.runBlocking

@Database(entities = [GroupEntity::class], version = 1)
abstract class GroupDatabase : RoomDatabase() {

    abstract fun groupDao(): GroupDao

    companion object {

        fun getInstance(context: Context): GroupDatabase = Room
            .databaseBuilder(context, GroupDatabase::class.java, GroupContract.DATABASE_NAME)
            .addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    val tempImageUrl = "https://avatars.githubusercontent.com/u/38340588?v=4"
                    val dummyData = listOf(
                        GroupEntity(
                            name = "캡짱모임",
                            isCompleted = false,
                            subject = "✈️",
                            memberProfileImage = tempImageUrl
                        ),
                        GroupEntity(
                            name = "모임 01",
                            isCompleted = true,
                            subject = "😊",
                            memberProfileImage = tempImageUrl
                        ),
                        GroupEntity(
                            name = "모임 02",
                            isCompleted = false,
                            subject = "😊",
                            memberProfileImage = tempImageUrl
                        ),
                        GroupEntity(
                            name = "모임 03",
                            isCompleted = true,
                            subject = "✈️",
                            memberProfileImage = ""
                        )
                    )

                    Executors.newSingleThreadExecutor().execute {
                        runBlocking {
                            getInstance(context).groupDao().apply {
                                dummyData.forEach { addGroup(it) }
                            }
                        }
                    }
                }
            })
            .build()
    }
}
