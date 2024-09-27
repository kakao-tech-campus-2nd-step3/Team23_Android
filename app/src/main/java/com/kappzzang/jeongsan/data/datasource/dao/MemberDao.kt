package com.kappzzang.jeongsan.data.datasource.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kappzzang.jeongsan.data.datasource.MemberContract
import com.kappzzang.jeongsan.data.entity.MemberEntity

@Dao
interface MemberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMember(memberEntity: MemberEntity)

    @Delete
    fun deleteMember(memberEntity: MemberEntity)

    @Query("SELECT * FROM ${MemberContract.MemberEntry.TABLE_NAME}")
    fun getAllMember(): List<MemberEntity>
}
