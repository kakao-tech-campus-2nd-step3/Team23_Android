package com.kappzzang.jeongsan.expenselist.inviteinfo

import androidx.recyclerview.widget.DiffUtil
import com.kappzzang.jeongsan.model.MemberItem

class MemberInfoDiffUtil : DiffUtil.ItemCallback<MemberItem>() {

    override fun areItemsTheSame(oldItem: MemberItem, newItem: MemberItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MemberItem, newItem: MemberItem): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: MemberItem, newItem: MemberItem): Any? =
        super.getChangePayload(oldItem, newItem)
}
