package com.kappzzang.jeongsan.usecase

import android.util.Log
import com.kappzzang.jeongsan.model.TransferDetailItem
import com.kappzzang.jeongsan.repository.TransferRepository
import com.kappzzang.jeongsan.repository.UserInfoRepository
import javax.inject.Inject

class SendTransferMessageUseCase @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val transferRepository: TransferRepository
) {
    suspend operator fun invoke(transferInfoList: List<TransferDetailItem>): Boolean {
        val requestUser = userInfoRepository.getUserInfo()
        if (requestUser == null) {
            Log.d(TAG, "requestUser is null")
            return false
        }

        val transferLink = transferRepository.getTransferLink(requestUser.uuid)
        if (transferLink == null) {
            Log.d("SendTransferMessageUseCase", "transferLink is null")
            return false
        }

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
