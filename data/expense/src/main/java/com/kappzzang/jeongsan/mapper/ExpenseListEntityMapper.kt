package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.entity.expenselist.ExpenseRemoteEntity
import com.kappzzang.jeongsan.model.ExpenseItem
import com.kappzzang.jeongsan.model.ExpenseItemWithCategory
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.util.ColorParser
import com.kappzzang.jeongsan.util.DateConverter

object ExpenseListEntityMapper {

    fun mapExpenseEntityToModel(
        entity: ExpenseRemoteEntity,
        serviceId: String,
        checked: Boolean = false
    ): ExpenseItemWithCategory = ExpenseItemWithCategory(
        item = ExpenseItem(
            id = entity.id.toString(),
            name = entity.title,
            price = entity.totalPrice,
            state = mapExpenseStateToDomainState(entity.state, checked)
        ),
        date = DateConverter.parseFromString(entity.createdAt),
        categoryColor = ColorParser.parseColor(entity.category.color),
        isMyPayment = (entity.payerServiceId?.toString() == serviceId) && (serviceId.isNotEmpty()),
        personalExpense = entity.myExpense
    )

    private fun mapExpenseStateToDomainState(state: String, checked: Boolean): ExpenseState {
        val trimmed = state.lowercase().trim()
        return when (trimmed) {
            "pending" -> ExpenseState.TRANSFER_PENDING
            "completed" -> ExpenseState.TRANSFERED
            "ongoing" -> {
                if (checked) ExpenseState.CONFIRMED else ExpenseState.NOT_CONFIRMED
            }

            else -> {
                throw IllegalStateException("Invalid Expense State")
            }
        }
    }
}
