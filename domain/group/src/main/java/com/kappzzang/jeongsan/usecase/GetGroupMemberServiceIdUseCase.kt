package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.repository.GroupInfoRepository
import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject

class GetGroupMemberServiceIdUseCase @Inject constructor(
    private val groupInfoRepository: GroupInfoRepository,
    private val authenticationRepository: AuthenticationRepository
) {

    suspend operator fun invoke(groupId: String, excludeMyId: Boolean = false): List<String> {
        val memberServiceIdList = groupInfoRepository.getMemberServiceIdList(groupId)

        return if (excludeMyId) {
            removeMyId(memberServiceIdList)
        } else {
            memberServiceIdList
        }
    }

    private fun removeMyId(memberServiceIdList: List<String>): List<String> {
        val myServiceId = authenticationRepository.getServiceId()
        return memberServiceIdList.filter { it != myServiceId }
    }
}
