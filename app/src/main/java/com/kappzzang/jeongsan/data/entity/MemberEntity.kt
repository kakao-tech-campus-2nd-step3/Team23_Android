package com.kappzzang.jeongsan.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kappzzang.jeongsan.data.datasource.MemberContract
import com.kappzzang.jeongsan.domain.model.MemberItem

@Entity(tableName = MemberContract.MemberEntry.TABLE_NAME)
class MemberEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(MemberContract.MemberEntry.COLUMN_NAME) val name: String,
    @ColumnInfo(MemberContract.MemberEntry.COLUMN_PROFILE_IMAGE_URL) val profileImageUrl: String,
    @ColumnInfo(MemberContract.MemberEntry.COLUMN_IS_INVITED) val isInvited: Boolean
)

fun MemberEntity.toVO(): MemberItem = MemberItem(
        id,
        name,
        profileImageUrl,
        isInvited
    )
