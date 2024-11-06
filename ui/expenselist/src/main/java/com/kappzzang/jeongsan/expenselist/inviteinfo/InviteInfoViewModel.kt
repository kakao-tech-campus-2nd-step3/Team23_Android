package com.kappzzang.jeongsan.expenselist.inviteinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.model.MemberItem
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
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _inviteInfo = MutableStateFlow<List<MemberItem>>(emptyList())
    val inviteInfo: StateFlow<List<MemberItem>> = _inviteInfo

    fun getInviteInfo(groupId: String) {
        viewModelScope.launch(ioDispatcher) { _inviteInfo.emit(getInviteInfoUseCase.invoke(groupId)) }
    }

    fun sendInviteMessage(groupId: String, groupName: String, memberId: String) =
        viewModelScope.launch {
            sendInviteMessageUseCase.invoke(groupId, groupName, memberId)
        }
}
