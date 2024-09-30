package com.kappzzang.jeongsan.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kappzzang.jeongsan.data.datasource.expense.ExpenseContract

@Entity(tableName = ExpenseContract.ExpenseEntity.TABLE_NAME)
class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ExpenseContract.ExpenseEntity.COLUMN_ID) var id: Long = 0,
    @ColumnInfo(name = ExpenseContract.ExpenseEntity.COLUMN_NAME) var name: String = "",
    @ColumnInfo(name = ExpenseContract.ExpenseEntity.COLUMN_EXPENSE_STATE)
    var expenseState: Int = 0,
    @ColumnInfo(name = ExpenseContract.ExpenseEntity.COLUMN_CREATED_AT)
    var createdTime: String = "",
    @ColumnInfo(name = ExpenseContract.ExpenseEntity.COLUMN_TOTAL_PRICE)
    var totalPrice: Int = 0,
    @ColumnInfo(name = ExpenseContract.ExpenseEntity.COLUMN_IMAGE)
    var image: String = "",
    @ColumnInfo(name = ExpenseContract.ExpenseEntity.COLUMN_CATEGORY_NAME)
    var categoryName: String = "",
    @ColumnInfo(name = ExpenseContract.ExpenseEntity.COLUMN_CATEGORY_COLOR)
    var categoryColor: String = ""
)
