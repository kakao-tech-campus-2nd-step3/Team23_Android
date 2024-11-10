package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.repository.ExpenseMessageRepository
import com.kappzzang.jeongsan.repository.UserInfoRepository
import javax.inject.Inject

class SendNewExpenseMessageUseCase @Inject constructor(
    private val expenseMessageRepository: ExpenseMessageRepository,
    private val userInfoRepository: UserInfoRepository
) {

    suspend operator fun invoke(
        expenseId: String,
        expenseName: String,
        groupId: String,
        memberUuidList: List<String>,
    ): Boolean {
        val myUserInfo = userInfoRepository.getUserInfo() ?: return false
        return expenseMessageRepository.sendNewExpenseMessage(
            expenseId = expenseId,
            expenseName = expenseName,
            payerName = myUserInfo.name,
            groupId = groupId,
            memberUuidList = memberUuidList
        )
    }
}