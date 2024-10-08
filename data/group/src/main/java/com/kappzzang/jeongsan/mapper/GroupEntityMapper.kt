package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.entity.GroupEntity
import com.kappzzang.jeongsan.model.GroupCreateItem

object GroupEntityMapper {
    fun mapGroupCreateToGroupEntity(groupCreateItem: GroupCreateItem): GroupEntity = GroupEntity(
        name = groupCreateItem.name,
        isCompleted = false,
        subject = groupCreateItem.subject,
        memberProfileImage = "https://avatars.githubusercontent.com/u/38340588?v=4"
    )
}
