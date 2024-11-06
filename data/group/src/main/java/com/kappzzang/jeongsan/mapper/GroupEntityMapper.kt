package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.entity.GroupEntity
import com.kappzzang.jeongsan.entity.GroupInfo
import com.kappzzang.jeongsan.model.GroupCreateItem
import com.kappzzang.jeongsan.model.GroupItem

object GroupEntityMapper {
    fun mapGroupCreateToGroupEntity(groupCreateItem: GroupCreateItem): GroupEntity = GroupEntity(
        name = groupCreateItem.name,
        isCompleted = false,
        subject = groupCreateItem.subject,
        memberProfileImage = "https://avatars.githubusercontent.com/u/38340588?v=4"
    )

    fun GroupInfo.toGroupItem(): GroupItem {
        return GroupItem(
            id = id.toString(),
            name = name,
            isCompleted = isCompleted,
            subject = subject,
            profileImageURL = previewList.map { it.profileImageUrl }
        )
    }
}
