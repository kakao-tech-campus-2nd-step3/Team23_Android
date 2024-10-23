package com.kappzzang.jeongsan.creategroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.data.MemberUIData
import com.kappzzang.jeongsan.model.GroupCreateItem
import com.kappzzang.jeongsan.usecase.SendInviteMessageUseCase
import com.kappzzang.jeongsan.usecase.UploadGroupInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val uploadGroupInfoUseCase: UploadGroupInfoUseCase,
    private val sendInviteMessageUseCase: SendInviteMessageUseCase
) : ViewModel() {

    val groupName = MutableStateFlow("")
//    val groupName: StateFlow<String> = _groupName

    private val _groupId = MutableStateFlow("")
    val groupId = _groupId.asStateFlow()

    private val _groupSubject = MutableStateFlow("")
    val groupSubject: StateFlow<String> = _groupSubject

    private val _groupMemberList = MutableStateFlow<List<MemberUIData>>(emptyList())
    val groupMemberList: StateFlow<List<MemberUIData>> = _groupMemberList

//    fun updateGroupName(name: String) {
//        viewModelScope.launch {
//            groupName.emit(name)
//        }
//    }

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
        if (!checkGroupInfoValidation()) {
            return false
        }

        val groupInfo = GroupCreateItem(
            name = groupName.value,
            subject = _groupSubject.value,
            memberIdList = _groupMemberList.value.map { it.uuid }
        )

        viewModelScope.launch(Dispatchers.IO) {
            uploadGroupInfoUseCase(groupInfo)
        }
        return true
    }

    fun sendInviteMessageAll(groupId: String) = _groupMemberList.value.forEach { member ->
        viewModelScope.launch {
            sendInviteMessageUseCase.invoke(groupId, groupName.value, member.uuid)
        }
    }

    private fun checkGroupInfoValidation(): Boolean = groupName.value.isNotEmpty() &&
        _groupSubject.value.isNotEmpty() &&
        _groupMemberList.value.isNotEmpty()
}
