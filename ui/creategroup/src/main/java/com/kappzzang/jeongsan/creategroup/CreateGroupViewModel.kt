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

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val uploadGroupInfoUseCase: UploadGroupInfoUseCase,
    private val sendInviteMessageUseCase: SendInviteMessageUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

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

    fun uploadGroupInfo(): Boolean {
        val groupInfo = GroupCreateItem(
            name = groupName.value,
            subject = _groupSubject.value,
            memberUuidList = _groupMemberList.value.map { it.uuid }
        )
        var isSuccess = false
        viewModelScope.launch(ioDispatcher) {
            isSuccess = try {
                val result = uploadGroupInfoUseCase(groupInfo)
                _groupId.value = result.toString()
                true
            } catch (e: Exception) {
                false
            }
        }
        return isSuccess
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
