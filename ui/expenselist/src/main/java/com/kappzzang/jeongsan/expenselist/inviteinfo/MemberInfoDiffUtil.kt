package com.kappzzang.jeongsan.expenselist.inviteinfo

import androidx.recyclerview.widget.DiffUtil
import com.kappzzang.jeongsan.model.MemberItem

class MemberInfoDiffUtil : DiffUtil.ItemCallback<com.kappzzang.jeongsan.model.MemberItem>() {

    override fun areItemsTheSame(oldItem: com.kappzzang.jeongsan.model.MemberItem, newItem: com.kappzzang.jeongsan.model.MemberItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: com.kappzzang.jeongsan.model.MemberItem, newItem: com.kappzzang.jeongsan.model.MemberItem): Boolean =
        oldItem.name == newItem.name &&
            oldItem.profileImageURL == newItem.profileImageURL &&
            oldItem.isInvited == newItem.isInvited

    override fun getChangePayload(oldItem: com.kappzzang.jeongsan.model.MemberItem, newItem: com.kappzzang.jeongsan.model.MemberItem): Any? =
        super.getChangePayload(oldItem, newItem)
}
