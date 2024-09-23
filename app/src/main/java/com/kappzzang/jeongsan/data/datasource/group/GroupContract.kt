package com.kappzzang.jeongsan.data.datasource.group

object GroupContract {
    const val DATABASE_NAME = "group.db"

    object GroupEntity {
        const val TABLE_NAME = "group"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_IS_COMPLETED = "is_completed"
        const val COLUMN_SUBJECT = "subject"
        const val COLUMN_MEMBER_PROFILE_IMAGE = "member_profile_image"
    }
}
