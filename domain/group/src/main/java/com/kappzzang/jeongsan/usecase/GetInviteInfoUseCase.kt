package com.kappzzang.jeongsan.usecase

class GetInviteInfoUseCase(
    private val memberRepository: com.kappzzang.jeongsan.repository.MemberRepository
) {
    suspend operator fun invoke(): List<com.kappzzang.jeongsan.model.MemberItem> =
        memberRepository.getAllMember()

    // 더미 데이터 삽입용 임시 함수
    suspend fun insertDummyData() {
        for (i in 1..10) {
            memberRepository.addMember(
                com.kappzzang.jeongsan.model.MemberItem(
                    id = "id$i",
                    name = "멤버 이름$i",
                    profileImageURL = "",
                    isInvited = false
                )
            )
        }
    }
}
