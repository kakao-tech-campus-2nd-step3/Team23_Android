package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.repository.InviteRepository
import javax.inject.Inject

class SendInviteMessageUseCase @Inject constructor(
    private val inviteRepository: InviteRepository
) {
    suspend operator fun invoke(id: String): Boolean {
        val inviteLink = inviteRepository.getInviteLink()
        return inviteRepository.sendInviteMessage(
            listOf(id),
            inviteLink,
            "모임 이름"
        )
    }
}