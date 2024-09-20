package com.kappzzang.jeongsan.domain.model

data class GroupItem(
    val id: String,
    val name: String,
    val isCompleted: Boolean,
    val profileImageURL: List<String>
)
