package com.kappzzang.jeongsan.creategroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.data.MemberUIData
import com.kappzzang.jeongsan.model.GroupCreateItem
import com.kappzzang.jeongsan.usecase.SendInviteMessageUseCase
import com.kappzzang.jeongsan.usecase.UploadGroupInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class GroupUploadState { IDLE, UPLOADING, SUCCESS, FAILED }

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val uploadGroupInfoUseCase: UploadGroupInfoUseCase,
    private val sendInviteMessageUseCase: SendInviteMessageUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _groupUploadState = MutableStateFlow(GroupUploadState.IDLE)
    val groupUploadState = _groupUploadState.asStateFlow()

    val groupName = MutableStateFlow("")

    private val _groupId = MutableStateFlow("")
    val groupId = _groupId.asStateFlow()

    private val _groupSubject = MutableStateFlow("")
    val groupSubject: StateFlow<String> = _groupSubject

    private val _groupMemberList = MutableStateFlow<List<MemberUIData>>(emptyList())
    val groupMemberList: StateFlow<List<MemberUIData>> = _groupMemberList

    fun updateGroupSubject(subject: String) {
        viewModelScope.launch {
            _groupSubject.emit(subject)
        }
    }

    fun updateGroupMemberList(memberList: List<MemberUIData>) {
        viewModelScope.launch {
            _groupMemberList.emit(memberList)
        }
    }

    fun removeMember(removedMemberPosition: Int) {
        viewModelScope.launch {
            _groupMemberList.emit(
                _groupMemberList.value.filterIndexed { index, _ -> index != removedMemberPosition }
            )
        }
    }

    fun uploadGroupInfo() {
        if (_groupUploadState.value == GroupUploadState.UPLOADING) {
            return
        }
        val groupInfo = GroupCreateItem(
            name = groupName.value,
            subject = _groupSubject.value,
            memberUuidList = _groupMemberList.value.map { it.uuid }
        )

        _groupUploadState.value = GroupUploadState.UPLOADING
        viewModelScope.launch(ioDispatcher) {
            try {
                val result = uploadGroupInfoUseCase(groupInfo)
                _groupUploadState.value = GroupUploadState.SUCCESS
            } catch (e: Exception) {
                _groupUploadState.value = GroupUploadState.FAILED
            }
        }
    }

    fun sendInviteMessageAll(groupId: String) = viewModelScope.launch {
        sendInviteMessageUseCase.invoke(
            groupId,
            groupName.value,
            _groupMemberList.value.map { it.uuid }
        )
    }

    fun checkGroupInfoValidation(): Boolean = groupName.value.isNotEmpty() &&
        _groupSubject.value.isNotEmpty() &&
        _groupMemberList.value.isNotEmpty()
}
