package com.kappzzang.jeongsan.datasource.expense

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseDao {
    @Insert
    fun addExpense(expenseEntity: com.kappzzang.jeongsan.entity.ExpenseEntity)

    @Delete
    fun deleteExpense(expenseEntity: com.kappzzang.jeongsan.entity.ExpenseEntity)

    @Query("SELECT * FROM `${ExpenseContract.ExpenseEntity.TABLE_NAME}` WHERE state = 0")
    fun getConfirmedExpense(): List<com.kappzzang.jeongsan.entity.ExpenseEntity>

    @Query("SELECT * FROM `${ExpenseContract.ExpenseEntity.TABLE_NAME}` WHERE state = 1")
    fun getNotConfirmedExpense(): List<com.kappzzang.jeongsan.entity.ExpenseEntity>

    @Query("SELECT * FROM `${ExpenseContract.ExpenseEntity.TABLE_NAME}` WHERE state = 2")
    fun getPendingExpense(): List<com.kappzzang.jeongsan.entity.ExpenseEntity>

    @Query("SELECT * FROM `${ExpenseContract.ExpenseEntity.TABLE_NAME}` WHERE state = 3")
    fun getTransferredExpense(): List<com.kappzzang.jeongsan.entity.ExpenseEntity>
}
