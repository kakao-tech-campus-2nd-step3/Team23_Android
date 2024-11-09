package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.TransferDetailItem
import com.kappzzang.jeongsan.repository.TransferRepository
import com.kappzzang.jeongsan.repository.UserInfoRepository
import javax.inject.Inject

class SendTransferMessageUseCase @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val transferRepository: TransferRepository
) {
    suspend operator fun invoke(transferInfoList: List<TransferDetailItem>): Boolean {
        val requestUser = userInfoRepository.getUserInfo() ?: return false
        val transferLink = transferRepository.getTransferLink(requestUser.serviceId) ?: return false

        return transferRepository.sendTransferMessage(
            transferInfoList,
            transferLink,
            requestUser.name
        )
    }

    companion object {
        private const val TAG = "SendTransferMessageUseCase"
    }
}
