package com.kappzzang.jeongsan.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.data.GroupViewItem
import com.kappzzang.jeongsan.usecase.GetDoneGroupUseCase
import com.kappzzang.jeongsan.usecase.GetProgressingGroupUseCase
import com.kappzzang.jeongsan.usecase.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val getProgressingGroupUseCase: GetProgressingGroupUseCase,
    private val getDoneGroupUseCase: GetDoneGroupUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _userProfileUrl = MutableStateFlow("")
    val userProfileUrl: StateFlow<String> = _userProfileUrl

    private val _groupList = MutableStateFlow<List<GroupViewItem>>(emptyList())
    val groupList: StateFlow<List<GroupViewItem>> = _groupList

    init {
        loadUserInfo()
        loadGroupList()
    }

    private fun loadUserInfo() {
        viewModelScope.launch {
            val userInfo = getUserInfoUseCase()
            _userName.value = userInfo?.name ?: "알 수 없음"
            _userProfileUrl.value = userInfo?.profileUrl ?: ""
        }
    }

    private fun loadGroupList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
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
}
