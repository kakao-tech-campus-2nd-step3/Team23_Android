package com.kappzzang.jeongsan.model

data class GroupCreateItem(
    val name: String,
    val subject: String,
    val memberServiceIdList: List<String>
)
