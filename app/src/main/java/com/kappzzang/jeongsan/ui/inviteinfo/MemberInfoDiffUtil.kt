package com.kappzzang.jeongsan.ui.inviteinfo

import androidx.recyclerview.widget.DiffUtil
import com.kappzzang.jeongsan.domain.model.MemberItem

class MemberInfoDiffUtil : DiffUtil.ItemCallback<MemberItem>() {

    override fun areItemsTheSame(oldItem: MemberItem, newItem: MemberItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MemberItem, newItem: MemberItem): Boolean =
        oldItem.name == newItem.name &&
            oldItem.profileImageURL == newItem.profileImageURL &&
            oldItem.isInvited == newItem.isInvited

    override fun getChangePayload(oldItem: MemberItem, newItem: MemberItem): Any? =
        super.getChangePayload(oldItem, newItem)
}
