package com.kappzzang.jeongsan.creategroup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.creategroup.databinding.ActivityCreateGroupBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateGroupActivity : AppCompatActivity() {
    private val viewModel: CreateGroupViewModel by viewModels()
    private lateinit var binding: ActivityCreateGroupBinding
    private lateinit var memberAdapter: MemberAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSpinner()
        initRecyclerView()
        setGroupNameObserver()
        setPickerButton()
        setCreateGroupButton()
    }

    private fun initSpinner() {
        ArrayAdapter.createFromResource(
            this@CreateGroupActivity,
            R.array.create_group_category_items,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.groupCategoryContentSpinner.adapter = adapter
            binding.groupCategoryContentSpinner.setSelection(adapter.count - 1)
        }

        binding.groupCategoryContentSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    val targetEmoji = selectedItem.substringBefore(' ')
                    viewModel.updateGroupSubject(targetEmoji)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.d("CreateGroupActivity", "Nothing selected")
                }
            }
    }

    private fun initRecyclerView() {
        memberAdapter = MemberAdapter(viewModel::removeMember)
        binding.memberContentRecyclerview.apply {
            adapter = memberAdapter
            layoutManager = LinearLayoutManager(this@CreateGroupActivity)
        }

        lifecycleScope.launch {
            viewModel.groupMemberList.collect { memberList ->
                memberAdapter.submitList(memberList)
            }
        }
    }

    private fun setGroupNameObserver() {
        binding.groupNameValueEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.updateGroupName(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setPickerButton() {
        binding.addMemberButton.setOnClickListener {
            viewModel.pickGroupMember()
        }
    }

    private fun setCreateGroupButton() {
        binding.createGroupButton.setOnClickListener {
            val isUploadSuccess = viewModel.uploadGroupInfo()
            if (isUploadSuccess) {
                finish()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.create_group_empty_info),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
