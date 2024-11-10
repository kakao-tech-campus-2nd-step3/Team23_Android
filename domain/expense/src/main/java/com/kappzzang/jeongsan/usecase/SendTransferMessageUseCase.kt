package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.TransferDetailItem
import com.kappzzang.jeongsan.model.UserFriendItem
import com.kappzzang.jeongsan.repository.TransferRepository
import com.kappzzang.jeongsan.repository.UserInfoRepository
import javax.inject.Inject

class SendTransferMessageUseCase @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val transferRepository: TransferRepository
) {
    private fun mapServiceIdToUuid(
        serviceIdList: List<String>,
        friends: List<UserFriendItem>
    ): List<String> =
        serviceIdList.mapNotNull { serviceId ->
            friends.find { friend ->
                friend.serviceId == serviceId
            }?.uuid
        }

    suspend operator fun invoke(transferInfoList: List<TransferDetailItem>): Result<Unit> {
        val requestUser = userInfoRepository.getUserInfo() ?: return Result.failure(
            IllegalStateException("유저 정보를 찾을 수 없습니다.")
        )
        val friends = userInfoRepository.getFriendList()
            ?: return Result.failure(
                IllegalStateException("친구 리스트를 불러오는 데 실패했습니다.")
            )
        val transferLink =
            transferRepository.getTransferLink(requestUser.serviceId)
                ?: return Result.failure(
                    IllegalStateException("송금 링크를 조회하는데 실패했습니다.")
                )
        val friendUuidList = mapServiceIdToUuid(transferInfoList.map { it.serviceId }, friends)

        return transferRepository.sendTransferMessage(
            transferLink = transferLink,
            friendUuidList = friendUuidList,
            payeeName = requestUser.name
        )
    }

    companion object {
        private const val TAG = "SendTransferMessageUseCase"
    }
}
