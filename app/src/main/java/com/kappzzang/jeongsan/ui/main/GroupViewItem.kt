package com.kappzzang.jeongsan.ui.main

import com.kappzzang.jeongsan.model.GroupItem

sealed class GroupViewItem {
    data object ProgressTitle : GroupViewItem()
    data object DoneTitle : GroupViewItem()
    data class Group(val groupItem: com.kappzzang.jeongsan.model.GroupItem) : GroupViewItem()
}
