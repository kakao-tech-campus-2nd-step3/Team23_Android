package com.kappzzang.jeongsan.domain.usecase

import com.kappzzang.jeongsan.domain.model.MemberItem
import com.kappzzang.jeongsan.domain.repository.MemberRepository
import javax.inject.Inject

class GetInviteInfoUseCase(
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(): List<MemberItem> {
        return memberRepository.getAllMember()
    }
}