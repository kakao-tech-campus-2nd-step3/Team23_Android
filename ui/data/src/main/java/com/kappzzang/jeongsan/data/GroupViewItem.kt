package com.kappzzang.jeongsan.data

import com.kappzzang.jeongsan.model.GroupItem

sealed class GroupViewItem {
    data class ProgressTitle(val isHide: Boolean) : GroupViewItem()
    data class DoneTitle(val isHide: Boolean) : GroupViewItem()
    data class Group(val groupItem: GroupItem) : GroupViewItem()
}
