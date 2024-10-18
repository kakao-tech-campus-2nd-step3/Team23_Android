package com.kappzzang.jeongsan.expenselist.inviteinfo

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.expenselist.ExpenseListViewModel
import com.kappzzang.jeongsan.expenselist.databinding.FragmentInviteInfoDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InviteInfoDialogFragment : DialogFragment() {

    private val inviteViewModel: InviteInfoViewModel by viewModels()
    private val expenseViewModel: ExpenseListViewModel by activityViewModels()
    private lateinit var binding: FragmentInviteInfoDialogBinding
    private lateinit var memberAdapter: MemberInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInviteInfoDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDialogStyle()
        initRecyclerView()
        setCloseButton()
        Log.d(
            TAG,
            "id: ${expenseViewModel.groupId.value}, name: ${expenseViewModel.groupName.value}"
        )
    }

    private fun setDialogStyle() {
        dialog?.window?.let { window ->
            window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

            val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
            val height = (resources.displayMetrics.heightPixels * 0.9).toInt()
            window.setLayout(width, height)
        }
    }

    private fun initRecyclerView() {
        memberAdapter = MemberInfoAdapter { memberId ->
            inviteViewModel.sendInviteMessage(
                expenseViewModel.groupId.value,
                expenseViewModel.groupName.value,
                memberId
            )
        }
        binding.memberContentRecyclerview.apply {
            adapter = memberAdapter
            layoutManager = LinearLayoutManager(context)
        }

        lifecycleScope.launch {
            inviteViewModel.inviteInfo.collect { inviteInfo ->
                memberAdapter.submitList(inviteInfo)
            }
        }
    }

    private fun setCloseButton() {
        binding.closeImageview.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        private const val TAG = "INVITE_INFO_DIALOG_FRAGMENT"
    }
}
