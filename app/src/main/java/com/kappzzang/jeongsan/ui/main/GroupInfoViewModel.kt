package com.kappzzang.jeongsan.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.domain.usecase.GetDoneGroupUseCase
import com.kappzzang.jeongsan.domain.usecase.GetProgressingGroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GroupInfoViewModel @Inject constructor(
    private val getProgressingGroupUseCase: GetProgressingGroupUseCase,
    private val getDoneGroupUseCase: GetDoneGroupUseCase
) : ViewModel() {

    private val _groupList = MutableStateFlow<List<GroupViewItem>>(emptyList())
    val groupList: StateFlow<List<GroupViewItem>> = _groupList

    init {
        loadGroupList()
    }

    private fun loadGroupList() {
        viewModelScope.launch {
            val resultGroupList = mutableListOf<GroupViewItem>()

            val progressingGroupList = getProgressingGroupUseCase()
            if (progressingGroupList.isNotEmpty()) {
                resultGroupList.add(GroupViewItem.ProgressTitle)
                resultGroupList.addAll(progressingGroupList.map { GroupViewItem.Group(it) })
            }

            val doneGroupList = getDoneGroupUseCase()
            if (doneGroupList.isNotEmpty()) {
                resultGroupList.add(GroupViewItem.DoneTitle)
                resultGroupList.addAll(doneGroupList.map { GroupViewItem.Group(it) })
            }

            _groupList.value = resultGroupList
        }
    }
}
