package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.repository.GroupInfoRepository
import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject

class GetGroupMemberServiceIdUseCase @Inject constructor(
    private val groupInfoRepository: GroupInfoRepository,
    private val authenticationRepository: AuthenticationRepository
) {

    suspend operator fun invoke(groupId: String, excludeMyId: Boolean = false): List<String> =
        groupInfoRepository.getMemberServiceIdList(groupId).fold(
            onSuccess = { memberServiceIds ->
                if (excludeMyId) {
                    removeMyId(memberServiceIds)
                } else {
                    memberServiceIds
                }
            },
            onFailure = { emptyList() }
        )

    private fun removeMyId(memberServiceIdList: List<String>): List<String> {
        val myServiceId = authenticationRepository.getServiceId()
        return memberServiceIdList.filter { it != myServiceId }
    }
}
