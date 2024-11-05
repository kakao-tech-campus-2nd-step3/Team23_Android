package com.kappzzang.jeongsan.sendmessage

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.intentcontract.SendMessageContract
import com.kappzzang.jeongsan.navigation.MainPageNavigator
import com.kappzzang.jeongsan.navigation.SendMessageNavigator
import com.kappzzang.jeongsan.sendmessage.databinding.ActivitySendMessageBinding
import com.kappzzang.jeongsan.util.IntegerFormatter.formatDecimalSeparator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

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

        handleIntent()
        initRecyclerView()
        setTotalPriceObserver()
        setSendButton()

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
                viewModel.transferInfo.collect { memberAdapter.submitList(it) }
            }
        }
    }

    private fun setTotalPriceObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.totalPrice.collect { price ->
                    binding.totalPriceContentTextview.text = price.formatDecimalSeparator().plus(
                        getString(R.string.send_message_money_unit)
                    )
                }
            }
        }
    }

    private fun setSendButton() {
        binding.sendMessageButton.setOnClickListener {
            lifecycleScope.launch {
                if (viewModel.sendTransferMessage()) {
                    startSendCompleteActivity()
                } else {
                    Toast.makeText(
                        this@SendMessageActivity,
                        getString(R.string.send_message_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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
