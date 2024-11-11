package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.TransferDetailItem
import com.kappzzang.jeongsan.repository.TransferRepository
import javax.inject.Inject

class GetTransferInfoUseCase @Inject constructor(
    private val transferRepository: TransferRepository
) {
    suspend operator fun invoke(
        groupId: String,
        expenseIdList: List<String>
    ): Result<List<TransferDetailItem>> = transferRepository.getTransferInfo(
        groupId = groupId,
        expenseIdList = expenseIdList
    )
}
