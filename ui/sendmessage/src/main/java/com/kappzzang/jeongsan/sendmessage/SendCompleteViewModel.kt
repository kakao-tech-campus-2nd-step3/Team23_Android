package com.kappzzang.jeongsan.sendmessage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class SendCompleteViewModel @Inject constructor() : ViewModel() {
    private val _groupId = MutableStateFlow<String?>(null)
    val groupId: StateFlow<String?>
        get() = _groupId

    fun setGroupId(groupId: String?) {
        _groupId.value = groupId
    }
}
