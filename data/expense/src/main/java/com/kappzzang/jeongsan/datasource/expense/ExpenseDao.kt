package com.kappzzang.jeongsan.datasource.expense

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kappzzang.jeongsan.entity.expenselist.ExpenseRoomEntity

@Dao
interface ExpenseDao {
    @Insert
    fun addExpense(expenseEntity: ExpenseRoomEntity)

    @Delete
    fun deleteExpense(expenseEntity: ExpenseRoomEntity)

    @Query("SELECT * FROM `${ExpenseContract.ExpenseEntity.TABLE_NAME}` WHERE state = 0")
    fun getConfirmedExpense(): List<ExpenseRoomEntity>

    @Query("SELECT * FROM `${ExpenseContract.ExpenseEntity.TABLE_NAME}` WHERE state = 1")
    fun getNotConfirmedExpense(): List<ExpenseRoomEntity>

    @Query("SELECT * FROM `${ExpenseContract.ExpenseEntity.TABLE_NAME}` WHERE state = 2")
    fun getPendingExpense(): List<ExpenseRoomEntity>

    @Query("SELECT * FROM `${ExpenseContract.ExpenseEntity.TABLE_NAME}` WHERE state = 3")
    fun getTransferredExpense(): List<ExpenseRoomEntity>
}
