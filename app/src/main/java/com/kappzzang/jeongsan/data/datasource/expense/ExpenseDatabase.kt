package com.kappzzang.jeongsan.data.datasource.expense

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kappzzang.jeongsan.data.entity.ExpenseEntity
import com.kappzzang.jeongsan.domain.model.ExpenseState
import java.sql.Timestamp
import java.util.Date
import java.util.concurrent.Executors
import kotlinx.coroutines.runBlocking

private val nameList = listOf("스타벅스", "GS25", "빽다방", "롯데시네마", "CGV", "롯데리아", "버거킹")
private val colorList =
    listOf("#87A2FF", "#FFD7C4", "#87A2FF", "#987D9A", "#987D9A", "#BB9AB1", "#BB9AB1")
private val categoryNameList = listOf("커피", "편의점", "커피", "영화관", "영화관", "식당", "식당")

private fun makeFakeItemWithState(expenseState: ExpenseState, id: Int): ExpenseEntity {
    val adjustedIndex = (id + 1) * (ExpenseState.entries.indexOf(expenseState) + 1)

    return ExpenseEntity(
        name = nameList[adjustedIndex % nameList.size],
        totalPrice = 1200 * adjustedIndex,
        categoryColor = colorList[adjustedIndex % nameList.size],
        categoryName = categoryNameList[adjustedIndex % nameList.size],
        expenseState = expenseState.ordinal,
        createdTime = Timestamp(
            Date(2024, 9, 1 + adjustedIndex % 30, 10, (adjustedIndex * 15 % 60)).time
        ).toString()
    )
}

@Database(entities = [ExpenseEntity::class], version = 1)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    companion object {

        fun getInstance(context: Context): ExpenseDatabase = Room
            .databaseBuilder(context, ExpenseDatabase::class.java, ExpenseContract.DATABASE_NAME)
            .addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    val dummyDataConfirmed = (1..10).map {
                        makeFakeItemWithState(ExpenseState.CONFIRMED, it)
                    }
                    val dummyDatNotConfirmed = (11..15).map {
                        makeFakeItemWithState(ExpenseState.NOT_CONFIRMED, it)
                    }
                    val dummyDatTransferPending = (16..27).map {
                        makeFakeItemWithState(ExpenseState.TRANSFER_PENDING, it)
                    }
                    val dummyDatTransferred = (27..30).map {
                        makeFakeItemWithState(ExpenseState.TRANSFERED, it)
                    }

                    Executors.newSingleThreadExecutor().execute {
                        runBlocking {
                            getInstance(context).expenseDao().apply {
                                dummyDataConfirmed.forEach {
                                    addExpense(it)
                                }
                                dummyDatNotConfirmed.forEach {
                                    addExpense(it)
                                }
                                dummyDatTransferPending.forEach {
                                    addExpense(it)
                                }
                                dummyDatTransferred.forEach {
                                    addExpense(it)
                                }
                            }
                        }
                    }
                }
            })
            .build()
    }
}
