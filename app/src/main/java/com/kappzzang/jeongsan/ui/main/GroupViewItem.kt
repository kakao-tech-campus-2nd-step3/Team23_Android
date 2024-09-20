package com.kappzzang.jeongsan.ui.main

data class GroupViewItem(
    val id: String,
    val name: String,
    val isCompleted: Boolean,
    val profileImageURL: List<String>,
    val type: GroupViewType
)
