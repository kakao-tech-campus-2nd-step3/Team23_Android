package com.kappzzang.jeongsan.domain.model

data class GroupItem(
    val viewType: GroupViewType,
    val groupName: String,
    val profileImageURL: List<String>
)
