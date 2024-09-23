package com.kappzzang.jeongsan.ui.inviteinfo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.databinding.ActivityInviteInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InviteInfoActivity : AppCompatActivity() {

    private val viewModel: InviteInfoViewModel by viewModels()
    lateinit var binding: ActivityInviteInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInviteInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRecyclerView()

        val dm = applicationContext.resources.displayMetrics
        val width = (dm.widthPixels * 0.9).toInt()
        val height = (dm.heightPixels * 0.9).toInt()

        window.attributes.apply {
            this.width = width
            this.height = height
        }

        binding.closeImageview.setOnClickListener {
            finish()
        }
    }

    private fun setRecyclerView() {
        binding.memberContentRecyclerview.apply {
            val adapter = MemberInfoAdapter()
            this.adapter = adapter
            layoutManager = LinearLayoutManager(
                this@InviteInfoActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            viewModel.inviteInfo.observe(this@InviteInfoActivity) {
                adapter.submitList(it)
            }
            viewModel.getInviteInfo()
        }
    }

    // 추후 setRecyclerView함수에서 그룹원 조회를 위해 intent에서 Id를 받아오는 함수
    private fun getGroupId(): String? {
        return intent.getStringExtra("groupId")
    }
}
