package com.kappzzang.jeongsan.main.animator

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.kappzzang.jeongsan.main.GroupListAdapter.ViewType.GROUP

class NoAnimationInGroupTitleAnimator : DefaultItemAnimator() {
    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder?,
        newHolder: RecyclerView.ViewHolder?,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean = if (oldHolder?.itemViewType == GROUP && newHolder?.itemViewType == GROUP) {
        super.animateChange(oldHolder, newHolder, fromX, fromY, toX, toY)
    } else {
        dispatchChangeFinished(oldHolder, true)
        dispatchChangeFinished(newHolder, false)
        false
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder?) = when (holder?.itemViewType) {
        GROUP -> super.animateAdd(holder)
        else -> {
            dispatchAddFinished(holder)
            false
        }
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder?) = when (holder?.itemViewType) {
        GROUP -> super.animateRemove(holder)
        else -> {
            dispatchRemoveFinished(holder)
            false
        }
    }
}
