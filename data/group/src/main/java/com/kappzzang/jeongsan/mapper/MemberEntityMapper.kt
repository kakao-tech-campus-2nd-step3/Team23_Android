package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.entity.MemberEntity
import com.kappzzang.jeongsan.model.MemberItem

object MemberEntityMapper {
    fun mapMemberEntityToMember(memberEntity: MemberEntity): MemberItem = MemberItem(
        id = memberEntity.id,
        name = memberEntity.name,
        profileImageUrl = memberEntity.profileImageUrl,
        isInvited = memberEntity.isInvited
    )

    fun mapMemberToMemberEntity(memberItem: MemberItem) = MemberEntity(
        id = memberItem.id,
        name = memberItem.name,
        profileImageUrl = memberItem.profileImageUrl,
        isInvited = memberItem.isInvited
    )
}