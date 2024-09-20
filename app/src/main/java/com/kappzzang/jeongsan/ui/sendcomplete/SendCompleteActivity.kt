package com.kappzzang.jeongsan.ui.sendcomplete

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kappzzang.jeongsan.databinding.ActivitySendCompleteBinding
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
    }
}
