package com.kappzzang.jeongsan.datasource.group

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kappzzang.jeongsan.data.entity.GroupEntity

@Dao
interface GroupDao {
    @Insert
    fun addGroup(groupEntity: GroupEntity)

    @Delete
    fun deleteGroup(groupEntity: GroupEntity)

    @Query("SELECT * FROM `${GroupContract.GroupEntity.TABLE_NAME}` WHERE is_completed = 0")
    fun getProgressingGroup(): List<GroupEntity>

    @Query("SELECT * FROM `${GroupContract.GroupEntity.TABLE_NAME}` WHERE is_completed = 1")
    fun getDoneGroup(): List<GroupEntity>

    @Query("SELECT * FROM `${GroupContract.GroupEntity.TABLE_NAME}` WHERE id = :groupId")
    fun inquireGroupInfo(groupId: Long): List<GroupEntity>
}