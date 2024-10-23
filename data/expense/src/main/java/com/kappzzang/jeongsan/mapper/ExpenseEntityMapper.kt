package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.entity.ExpenseEntity
import com.kappzzang.jeongsan.model.ExpenseItem
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.util.DateConverter

object ExpenseEntityMapper {
    fun mapExpenseEntityToModel(entity: ExpenseEntity): ExpenseItem = ExpenseItem(
        id = entity.id.toString(),
        name = entity.name,
        categoryColor = entity.categoryColor,
        price = entity.totalPrice,
        expenseImageUrl = entity.image,
        date = DateConverter.parseFromString(entity.createdTime),
        payerMemberId = "",
        payerName = "",
        state = ExpenseState.entries[entity.expenseState]
    )
}
