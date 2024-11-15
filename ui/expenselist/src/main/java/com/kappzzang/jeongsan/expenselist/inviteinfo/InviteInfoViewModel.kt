package com.kappzzang.jeongsan.expenselist.inviteinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.data.ExpenseListUIState
import com.kappzzang.jeongsan.data.HasGroupId
import com.kappzzang.jeongsan.data.HasGroupInfo
import com.kappzzang.jeongsan.data.InviteMessageUiState
import com.kappzzang.jeongsan.model.MemberItem
import com.kappzzang.jeongsan.usecase.ConvertServiceIdToUuidUseCase
import com.kappzzang.jeongsan.usecase.GetInviteInfoUseCase
import com.kappzzang.jeongsan.usecase.SendInviteMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class InviteInfoViewModel @Inject constructor(
    private val getInviteInfoUseCase: GetInviteInfoUseCase,
    private val sendInviteMessageUseCase: SendInviteMessageUseCase,
    private val convertServiceIdToUuidUseCase: ConvertServiceIdToUuidUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _inviteInfo = MutableStateFlow<List<MemberItem>>(emptyList())
    val inviteInfo: StateFlow<List<MemberItem>> = _inviteInfo

    private val _inviteMessageState =
        MutableStateFlow<InviteMessageUiState>(InviteMessageUiState.Idle)
    val inviteMessageState: StateFlow<InviteMessageUiState> = _inviteMessageState

    fun getInviteInfo(groupId: String) {
        viewModelScope.launch(ioDispatcher) {
            _inviteInfo.emit(getInviteInfoUseCase.invoke(groupId))
        }
    }

    fun sendInviteMessageWithServiceId(
        expenseListState: ExpenseListUIState,
        memberServiceId: String
    ) {
        val groupId = (expenseListState as? HasGroupId)?.groupId ?: let {
            return
        }

        val groupName = (expenseListState as? HasGroupInfo)?.groupName ?: let {
            return
        }

        viewModelScope.launch(ioDispatcher) {
            sendInviteMessage(
                groupId,
                groupName,
                convertServiceIdToUuidUseCase(listOf(memberServiceId))
            )
        }
    }

    private fun sendInviteMessage(groupId: String, groupName: String, memberUuid: List<String>) =
        viewModelScope.launch(ioDispatcher) {
            val result = sendInviteMessageUseCase.invoke(groupId, groupName, memberUuid)
            _inviteMessageState.emit(
                if (result) {
                    InviteMessageUiState.Success
                } else {
                    InviteMessageUiState.Fail
                }
            )
        }

    fun clearInviteMessageState() {
        viewModelScope.launch(ioDispatcher) {
            _inviteMessageState.emit(InviteMessageUiState.Idle)
        }
    }
}
