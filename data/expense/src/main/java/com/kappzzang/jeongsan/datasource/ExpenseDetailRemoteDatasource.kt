package com.kappzzang.jeongsan.datasource

import com.kappzzang.jeongsan.api.ReceiptRetrofitService
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseDetailEntity
import retrofit2.Response
import javax.inject.Inject

class ExpenseDetailRemoteDatasource @Inject constructor(
    private val receiptRetrofitService: ReceiptRetrofitService
) {
    suspend fun getExpenseDetail(expenseId: String, jwt: String): Response<ExpenseDetailEntity> =
        receiptRetrofitService.getExpenseDetails(
            expenseId = expenseId,
            jwt = jwt
        )
}
