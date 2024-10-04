package com.kappzzang.jeongsan.data

import com.kappzzang.jeongsan.model.GroupItem

sealed class GroupViewItem {
    data object ProgressTitle : GroupViewItem()
    data object DoneTitle : GroupViewItem()
    data class Group(val groupItem: GroupItem) : GroupViewItem()
}
