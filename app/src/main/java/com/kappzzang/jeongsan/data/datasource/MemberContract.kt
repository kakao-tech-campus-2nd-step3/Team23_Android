package com.kappzzang.jeongsan.data.datasource

object MemberContract {
    const val DATABASE_NAME = "member.db"

    object MemberEntry {
        const val TABLE_NAME = "member"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PROFILE_IMAGE_URL = "profile_image_url"
        const val COLUMN_IS_INVITED = "is_invited"
    }
}