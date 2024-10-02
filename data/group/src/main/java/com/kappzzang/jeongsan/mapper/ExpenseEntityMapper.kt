package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.domain.model.ExpenseItem
import com.kappzzang.jeongsan.domain.model.ExpenseState
import com.kappzzang.jeongsan.domain.model.MemberItem
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
            payer = MemberItem("", "", "", false),
            state = ExpenseState.entries[entity.expenseState]
        )
}
