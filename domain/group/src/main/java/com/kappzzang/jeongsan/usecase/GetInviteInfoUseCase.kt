package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.MemberItem
import com.kappzzang.jeongsan.repository.MemberRepository

class GetInviteInfoUseCase(private val memberRepository: MemberRepository) {
    suspend operator fun invoke(): List<MemberItem> = memberRepository.getAllMember()

    // 더미 데이터 삽입용 임시 함수
    suspend fun insertDummyData() {
        for (i in 1..10) {
            memberRepository.addMember(
                MemberItem(
                    id = "id$i",
                    name = "멤버 이름$i",
                    profileImageURL = "",
                    isInvited = false
                )
            )
        }
    }
}
