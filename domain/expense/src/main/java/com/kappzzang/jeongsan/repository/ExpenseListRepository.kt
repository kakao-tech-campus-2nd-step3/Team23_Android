package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ExpenseState
import kotlinx.coroutines.flow.Flow

interface ExpenseListRepository {

    /**
     *  지출 목록 조회
     *  @param groupId 조회할 그룹 명
     *  @param expenseState 조회할 지출의 상태 (정산 중, 송금 요청, 송금 완료 ...)
     *  @return 지출 목록 response flow
     */
    fun getExpenseList(groupId: String, expenseState: ExpenseState): Flow<ExpenseListResponse>
}
