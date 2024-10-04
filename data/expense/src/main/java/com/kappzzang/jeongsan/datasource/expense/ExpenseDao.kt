package com.kappzzang.jeongsan.datasource.expense

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kappzzang.jeongsan.entity.ExpenseEntity

@Dao
interface ExpenseDao {
    @Insert
    fun addExpense(expenseEntity: ExpenseEntity)

    @Delete
    fun deleteExpense(expenseEntity: ExpenseEntity)

    @Query("SELECT * FROM `${ExpenseContract.ExpenseEntity.TABLE_NAME}` WHERE state = 0")
    fun getConfirmedExpense(): List<ExpenseEntity>

    @Query("SELECT * FROM `${ExpenseContract.ExpenseEntity.TABLE_NAME}` WHERE state = 1")
    fun getNotConfirmedExpense(): List<ExpenseEntity>

    @Query("SELECT * FROM `${ExpenseContract.ExpenseEntity.TABLE_NAME}` WHERE state = 2")
    fun getPendingExpense(): List<ExpenseEntity>

    @Query("SELECT * FROM `${ExpenseContract.ExpenseEntity.TABLE_NAME}` WHERE state = 3")
    fun getTransferredExpense(): List<ExpenseEntity>
}
