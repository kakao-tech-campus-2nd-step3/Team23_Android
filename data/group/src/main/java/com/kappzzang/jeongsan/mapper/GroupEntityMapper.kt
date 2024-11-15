package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.entity.GroupInfo
import com.kappzzang.jeongsan.entity.MemberServiceIdResponse
import com.kappzzang.jeongsan.model.GroupItem

object GroupEntityMapper {

    fun GroupInfo.toGroupItem() = GroupItem(
        id = id.toString(),
        name = name,
        isCompleted = isCompleted,
        subject = subject,
        profileImageURL = previewList.map { it.profileImageUrl }
    )

    fun MemberServiceIdResponse.toServiceIdList() = data.map {
        it.serviceId
    }
}
