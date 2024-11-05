package com.kappzzang.jeongsan.sendmessage

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kappzzang.jeongsan.intentcontract.SendMessageContract
import com.kappzzang.jeongsan.navigation.ExpenseListNavigator
import com.kappzzang.jeongsan.sendmessage.databinding.ActivitySendCompleteBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Rotation
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Shape
import nl.dionsegijn.konfetti.core.models.Size

@AndroidEntryPoint
class SendCompleteActivity : AppCompatActivity() {
    @Inject
    lateinit var expenseListNavigator: ExpenseListNavigator
    private val viewModel: SendCompleteViewModel by viewModels()
    private val binding: ActivitySendCompleteBinding by lazy {
        ActivitySendCompleteBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        handleIntent()
        setCongratulationsEffect()

        val closeTimer = getCountTimer()
        closeTimer.start()

        binding.closeButton.setOnClickListener {
            closeTimer.cancel()
            endActivity()
        }
    }

    private fun handleIntent() {
        val groupId = intent.getStringExtra(SendMessageContract.GROUP_ID)
        viewModel.setGroupId(groupId)
    }

    private fun setCongratulationsEffect() {
        binding.congratulations.start(
            Party(
                speed = 40f,
                maxSpeed = 70f,
                damping = 0.9f,
                angle = Angle.TOP,
                spread = 45,
                size = listOf(Size.SMALL, Size.LARGE, Size.LARGE),
                shapes = listOf(Shape.Square, Shape.Circle),
                timeToLive = 3000L,
                rotation = Rotation(),
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
                position = Position.Relative(0.5, 1.0)
            )
        )
    }

    private fun getCountTimer(): CountDownTimer {
        binding.timeProgressIndicator.max = 100
        binding.timeTextView.text = CLOSE_TIME.toString()

        return object : CountDownTimer(CLOSE_TIME * 1000L, TIME_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                val remainTime = (millisUntilFinished / 1000L) + 1L
                binding.timeTextView.text = remainTime.toString()
                val progress = (millisUntilFinished / (CLOSE_TIME * 1000.0) * 100).toInt()
                binding.timeProgressIndicator.progress = progress
            }

            override fun onFinish() {
                binding.timeTextView.text = "0"
                binding.timeProgressIndicator.progress = 0
                endActivity()
            }
        }
    }

    private fun endActivity() {
        viewModel.groupId.value?.let {
            startExpenseListActivity(it)
        }
    }
    private fun startExpenseListActivity(groupId: String) {
        val expenseIntent = expenseListNavigator.navigateToExpenseList(
            this@SendCompleteActivity,
            groupId
        )
        startActivity(expenseIntent)
        finish()
    }
    companion object {
        private const val CLOSE_TIME = 3
        private const val TIME_INTERVAL = 10L
    }
}
