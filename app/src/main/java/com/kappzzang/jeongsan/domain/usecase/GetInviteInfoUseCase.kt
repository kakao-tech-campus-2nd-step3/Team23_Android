package com.kappzzang.jeongsan.domain.usecase

import com.kappzzang.jeongsan.domain.repository.MemberRepository
import javax.inject.Inject

class GetInviteInfoUseCase(
    private val memberRepository: MemberRepository
) {
}