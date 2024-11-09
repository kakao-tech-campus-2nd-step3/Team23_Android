package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.MemberItem
import com.kappzzang.jeongsan.repository.MemberRepository

class GetInviteInfoUseCase(private val memberRepository: MemberRepository) {
    suspend operator fun invoke(groupId: String): List<MemberItem> =
        memberRepository.getAllMember(groupId)
}
