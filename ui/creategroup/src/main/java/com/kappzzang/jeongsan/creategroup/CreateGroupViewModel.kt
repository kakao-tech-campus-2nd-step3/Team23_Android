package com.kappzzang.jeongsan.creategroup

import android.app.Application
import android.widget.Toast
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
enum class SendMessageState { IDLE, SUCCESS, FAILED }

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val application: Application,
    private val uploadGroupInfoUseCase: UploadGroupInfoUseCase,
    private val sendInviteMessageUseCase: SendInviteMessageUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _groupUploadState = MutableStateFlow(GroupUploadState.IDLE)
    val groupUploadState = _groupUploadState.asStateFlow()

    private val _sendMessageState = MutableStateFlow(SendMessageState.IDLE)
    val sendMessageState = _sendMessageState.asStateFlow()

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
        var validMemberList = memberList
        if (!isValidMemberList(memberList)) {
            Toast.makeText(application, UNAUTHORIZED_MEMBER_EXISTS, Toast.LENGTH_SHORT).show()
            validMemberList = removeInvalidMember(memberList)
        }

        viewModelScope.launch {
            _groupMemberList.emit(validMemberList)
        }
    }

    // 모두 가입된 멤버인지 확인
    private fun isValidMemberList(memberList: List<MemberUIData>): Boolean =
        memberList.all { it.serviceId.isNotEmpty() }

    private fun removeInvalidMember(memberList: List<MemberUIData>): List<MemberUIData> =
        memberList.filter { it.serviceId.isNotEmpty() }

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
            memberServiceIdList = _groupMemberList.value.map { it.serviceId }
        )

        _groupUploadState.value = GroupUploadState.UPLOADING
        viewModelScope.launch(ioDispatcher) {
            try {
                val result = uploadGroupInfoUseCase(groupInfo)
                _groupId.value = result.toString()
                _groupUploadState.value = GroupUploadState.SUCCESS
            } catch (e: Exception) {
                _groupUploadState.value = GroupUploadState.FAILED
            }
        }
    }

    fun sendInviteMessageAll(groupId: String) = viewModelScope.launch {
        val result = sendInviteMessageUseCase.invoke(
            groupId,
            groupName.value,
            _groupMemberList.value.map { it.uuid }
        )
        _sendMessageState.emit(if (result) SendMessageState.SUCCESS else SendMessageState.FAILED)
    }

    fun checkGroupInfoValidation(): Boolean = groupName.value.isNotEmpty() &&
        _groupSubject.value.isNotEmpty() &&
        _groupMemberList.value.isNotEmpty()

    companion object {
        private const val UNAUTHORIZED_MEMBER_EXISTS = "가입하지 않은 멤버가 존재합니다!"
    }
}
