package com.kappzzang.jeongsan.datasource

import com.kappzzang.jeongsan.api.ReceiptRetrofitService
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseDetailEntity
import javax.inject.Inject
import retrofit2.Response

class ExpenseDetailRemoteDatasource @Inject constructor(
    private val receiptRetrofitService: ReceiptRetrofitService
) {
    suspend fun getExpenseDetail(expenseId: String, jwt: String): Response<ExpenseDetailEntity> =
        receiptRetrofitService.getExpenseDetail(
            expenseId = expenseId,
            jwt = jwt
        )
}
