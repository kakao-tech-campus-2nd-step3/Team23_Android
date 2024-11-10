package com.kappzzang.jeongsan.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.data.GroupViewItem
import com.kappzzang.jeongsan.data.JoinGroupUIState
import com.kappzzang.jeongsan.usecase.GetDoneGroupUseCase
import com.kappzzang.jeongsan.usecase.GetProgressingGroupUseCase
import com.kappzzang.jeongsan.usecase.GetUserInfoUseCase
import com.kappzzang.jeongsan.usecase.JoinGroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val getProgressingGroupUseCase: GetProgressingGroupUseCase,
    private val getDoneGroupUseCase: GetDoneGroupUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val joinGroupUseCase: JoinGroupUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _userProfileUrl = MutableStateFlow("")
    val userProfileUrl: StateFlow<String> = _userProfileUrl

    private val _groupList = MutableStateFlow<List<GroupViewItem>>(emptyList())
    val groupList: StateFlow<List<GroupViewItem>> = _groupList

    private val _joinGroupState = MutableStateFlow<JoinGroupUIState>(JoinGroupUIState.Idle)
    val joinGroupState: StateFlow<JoinGroupUIState> = _joinGroupState

    init {
        loadUserInfo()
        loadGroupList()
    }

    fun loadUserInfo() {
        viewModelScope.launch {
            val userInfo = getUserInfoUseCase()
            _userName.value = userInfo?.name ?: "알 수 없음"
            _userProfileUrl.value = userInfo?.profileUrl ?: ""
        }
    }

    fun loadGroupList() {
        viewModelScope.launch {
            withContext(ioDispatcher) {
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

    fun isAlreadyJoined(inviteGroupId: String): Boolean {
        Log.d(TAG, _groupList.value.size.toString())
        return _groupList.value.any {
            it is GroupViewItem.Group && it.groupItem.id == inviteGroupId
        }
    }

    fun joinGroup(groupId: String) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                joinGroupUseCase(groupId).onSuccess {
                    _joinGroupState.emit(JoinGroupUIState.Success(groupId))
                }.onFailure {
                    _joinGroupState.emit(
                        JoinGroupUIState.Error(
                            it.message ?: JOIN_GROUP_FAILED_MESSAGE
                        )
                    )
                }
            }
        }
    }

    fun clearJoinGroupState() {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                _joinGroupState.emit(JoinGroupUIState.Idle)
            }
        }
    }

    companion object {
        private const val TAG = "MAIN_PAGE_VIEW_MODEL"
        private const val JOIN_GROUP_FAILED_MESSAGE = "모임에 가입할 수 없습니다."
    }
}
