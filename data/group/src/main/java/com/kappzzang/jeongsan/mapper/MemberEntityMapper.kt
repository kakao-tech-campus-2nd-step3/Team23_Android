package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.entity.MemberInfo
import com.kappzzang.jeongsan.model.MemberItem

object MemberEntityMapper {

    fun MemberInfo.toMemberItem() = MemberItem(
        id = id,
        name = name,
        profileImageUrl = profileImageUrl,
        isInvited = isInvited
    )
}
