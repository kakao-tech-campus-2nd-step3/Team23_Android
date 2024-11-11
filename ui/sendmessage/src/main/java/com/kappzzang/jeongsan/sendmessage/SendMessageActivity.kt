package com.kappzzang.jeongsan.sendmessage

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.intentcontract.SendMessageContract
import com.kappzzang.jeongsan.navigation.SendMessageNavigator
import com.kappzzang.jeongsan.sendmessage.data.ErrorState
import com.kappzzang.jeongsan.sendmessage.data.HasTransferInfo
import com.kappzzang.jeongsan.sendmessage.data.TransferInfoUIState
import com.kappzzang.jeongsan.sendmessage.databinding.ActivitySendMessageBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SendMessageActivity : AppCompatActivity() {
    @Inject
    lateinit var sendMessageNavigator: SendMessageNavigator

    private val viewModel: SendMessageViewModel by viewModels()
    private lateinit var binding: ActivitySendMessageBinding
    private lateinit var memberAdapter: MemberAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        handleIntent()
        initRecyclerView()
        setSendButton()
        collectTransferInfoUIState()
    }

    private fun collectTransferInfoUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.transferInfoState.collect {
                    if (it is TransferInfoUIState.Idle) {
                        viewModel.startFetchUiState()
                    }
                    if (it is ErrorState) {
                        sendToast(it.message)
                    }
                    if (it is TransferInfoUIState.ExpenseStateUpdateSuccess) {
                        startSendCompleteActivity()
                    }
                }
            }
        }
    }

    private fun sendToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun handleIntent() {
        val groupId = intent.getStringExtra(SendMessageContract.GROUP_ID)
        viewModel.setGroupId(groupId)
    }

    private fun initRecyclerView() {
        memberAdapter = MemberAdapter()
        binding.infoContentRecyclerview.apply {
            adapter = memberAdapter
            layoutManager = LinearLayoutManager(this@SendMessageActivity)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.transferInfoState.collect {
                    if (it is HasTransferInfo) {
                        memberAdapter.submitList(it.transferInfoList)
                    }
                }
            }
        }
    }

    private fun setSendButton() {
        binding.sendMessageButton.setOnClickListener {
            viewModel.sendTransferMessage()
        }
    }

    private fun startSendCompleteActivity() {
        val resIntent = sendMessageNavigator.navigateToSendComplete(
            this@SendMessageActivity,
            viewModel.groupId.value
        )
        startActivity(resIntent)
        finish()
    }
}
