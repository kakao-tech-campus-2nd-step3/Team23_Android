package com.kappzzang.jeongsan.domain.model

data class MemberItem(
    val id: String,
    val name: String,
    val profileImageURL: String,
    val isInvited: Boolean
)
