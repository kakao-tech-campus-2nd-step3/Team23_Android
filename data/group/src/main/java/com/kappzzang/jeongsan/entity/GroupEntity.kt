package com.kappzzang.jeongsan.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = GroupContract.GroupEntity.TABLE_NAME)
class GroupEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(
        name = GroupContract.GroupEntity.COLUMN_ID
    ) var id: Long = 0,
    @ColumnInfo(
        name = GroupContract.GroupEntity.COLUMN_NAME
    ) var name: String = "",
    @ColumnInfo(
        name = GroupContract.GroupEntity.COLUMN_IS_COMPLETED
    )
    var isCompleted: Boolean = false,
    @ColumnInfo(
        name = GroupContract.GroupEntity.COLUMN_SUBJECT
    )
    var subject: String = "",
    @ColumnInfo(
        name = GroupContract.GroupEntity.COLUMN_MEMBER_PROFILE_IMAGE
    )
    var memberProfileImage: String = ""
)
