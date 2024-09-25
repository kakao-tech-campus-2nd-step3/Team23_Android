package com.kappzzang.jeongsan.data.datasource.expense

object ExpenseContract {
    const val DATABASE_NAME = "expense.db"

    object ExpenseEntity {
        const val TABLE_NAME = "expense"
        const val COLUMN_ID = "expense_id"
        const val COLUMN_NAME = "title"
        const val COLUMN_TOTAL_PRICE = "total_price"
        const val COLUMN_EXPENSE_STATE = "state"
        const val COLUMN_CREATED_AT = "created_at"
        const val COLUMN_CATEGORY_NAME = "category_name"
        const val COLUMN_CATEGORY_COLOR = "category_color"
    }
}
