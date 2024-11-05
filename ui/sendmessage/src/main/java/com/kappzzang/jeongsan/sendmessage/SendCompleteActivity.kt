package com.kappzzang.jeongsan.sendmessage

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.kappzzang.jeongsan.sendmessage.databinding.ActivitySendCompleteBinding
import java.util.concurrent.TimeUnit
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Rotation
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Shape
import nl.dionsegijn.konfetti.core.models.Size

class SendCompleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySendCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

        binding.timeProgressIndicator.max = 100
        binding.timeTextView.text = CLOSE_TIME.toString()

        val closeTimer = object : CountDownTimer(CLOSE_TIME * 1000L, TIME_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                val remainTime = (millisUntilFinished / 1000L) + 1L
                binding.timeTextView.text = remainTime.toString()
                val progress = (millisUntilFinished / (CLOSE_TIME * 1000.0) * 100).toInt()
                binding.timeProgressIndicator.progress = progress
            }

            override fun onFinish() {
                binding.timeTextView.text = "0"
                binding.timeProgressIndicator.progress = 0
                startActivity(Intent(this@SendCompleteActivity, SendMessageActivity::class.java))
            }
        }
        closeTimer.start()

        binding.closeButton.setOnClickListener {
            closeTimer.cancel()
            startActivity(Intent(this@SendCompleteActivity, SendMessageActivity::class.java))
        }
    }
    companion object {
        private const val CLOSE_TIME = 3
        private const val TIME_INTERVAL = 10L
    }
}
