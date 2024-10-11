package com.kappzzang.jeongsan.expenselist.sendmessage

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.expenselist.R
import com.kappzzang.jeongsan.expenselist.databinding.ActivitySendMessageBinding
import com.kappzzang.jeongsan.navigation.AppNavigator
import com.kappzzang.jeongsan.util.IntegerFormatter.formatDecimalSeparator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SendMessageActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: AppNavigator
    private val viewModel: SendMessageViewModel by viewModels()
    private lateinit var binding: ActivitySendMessageBinding
    private lateinit var memberAdapter: MemberAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        setTotalPriceObserver()
        setSendButton()
    }

    private fun initRecyclerView() {
        memberAdapter = MemberAdapter()
        binding.infoContentRecyclerview.apply {
            adapter = memberAdapter
            layoutManager = LinearLayoutManager(this@SendMessageActivity)
        }

        lifecycleScope.launch {
            viewModel.transferInfo.collect { memberAdapter.submitList(it) }
        }
    }

    private fun setTotalPriceObserver() {
        lifecycleScope.launch {
            viewModel.totalPrice.collect { price ->
                binding.totalPriceContentTextview.text = price.formatDecimalSeparator().plus(
                    getString(R.string.send_message_money_unit)
                )
            }
        }
    }

    private fun setSendButton() {
        binding.sendMessageButton.setOnClickListener {
            lifecycleScope.launch {
                if (viewModel.sendTransferMessage()) {
                    startActivity(navigator.navigateToSendComplete(this@SendMessageActivity))
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
}
