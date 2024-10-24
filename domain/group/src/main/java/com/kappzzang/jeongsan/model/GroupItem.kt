package com.kappzzang.jeongsan.model

data class GroupItem(
    val id: String,
    val name: String,
    val isCompleted: Boolean,
    val subject: String,
    val profileImageURL: List<String>
)
