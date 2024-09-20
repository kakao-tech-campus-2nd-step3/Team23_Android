package com.kappzzang.jeongsan.domain.usecase

import com.kappzzang.jeongsan.domain.model.GroupItem
import com.kappzzang.jeongsan.domain.repository.ExpenseListPageRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentGroupInfoUseCase(private val repository: ExpenseListPageRepository) {
    operator fun invoke(
        groupId: String
    ) : Flow<GroupItem> = repository.getGroupInfo(groupId)
}
