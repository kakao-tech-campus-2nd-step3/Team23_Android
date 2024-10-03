package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.datasource.ExpenseListFakeDatasource
import com.kappzzang.jeongsan.model.ReceiptItem
import com.kappzzang.jeongsan.repository.ReceiptRepository
import javax.inject.Inject

class ReceiptRepositoryImpl @Inject constructor(
    private val expenseDataSource: ExpenseListFakeDatasource
) : ReceiptRepository {

    override suspend fun uploadExpense(receiptItem: ReceiptItem): String =
        expenseDataSource.addExpense(receiptItem)
}
