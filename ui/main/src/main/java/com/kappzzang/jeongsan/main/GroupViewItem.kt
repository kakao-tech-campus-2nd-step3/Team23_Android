package com.kappzzang.jeongsan.main

import com.kappzzang.jeongsan.model.GroupItem

sealed class GroupViewItem {
    data object ProgressTitle : GroupViewItem()
    data object DoneTitle : GroupViewItem()
    data class Group(val groupItem: model.GroupItem) : GroupViewItem()
}
