package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.model.ExpenseItem
import com.kappzzang.jeongsan.model.ExpenseState
import java.sql.Timestamp

object ExpenseEntityMapper {
    fun mapExpenseEntityToModel(entity: com.kappzzang.jeongsan.entity.ExpenseEntity): ExpenseItem =
        ExpenseItem(
            id = entity.id.toString(),
            name = entity.name,
            categoryColor = entity.categoryColor,
            price = entity.totalPrice,
            expenseImageUrl = entity.image,
            date = Timestamp.valueOf(entity.createdTime),
            payerMemberId = "",
            payerName = "",
            state = ExpenseState.entries[entity.expenseState]
        )
}
