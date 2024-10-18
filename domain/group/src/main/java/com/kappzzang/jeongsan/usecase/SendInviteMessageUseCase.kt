package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.repository.InviteRepository
import javax.inject.Inject

class SendInviteMessageUseCase @Inject constructor(private val inviteRepository: InviteRepository) {
    suspend operator fun invoke(groupId: String, groupName: String, memberId: String): Boolean =
        inviteRepository.sendInviteMessage(
            groupId,
            groupName,
            memberId
        )
}
