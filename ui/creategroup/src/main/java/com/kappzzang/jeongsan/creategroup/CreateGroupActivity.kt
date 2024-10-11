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
import com.kakao.sdk.friend.client.PickerClient
import com.kakao.sdk.friend.model.OpenPickerFriendRequestParams
import com.kakao.sdk.friend.model.PickerOrientation
import com.kakao.sdk.friend.model.SelectedUsers
import com.kakao.sdk.friend.model.ViewAppearance
import com.kappzzang.jeongsan.creategroup.databinding.ActivityCreateGroupBinding
import com.kappzzang.jeongsan.data.MemberUIData
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
                    id: Long,
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
            pickGroupMember()
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

    private fun pickGroupMember() {
        PickerClient.instance.selectFriendsPopup(
            context = this,
            params = openPickerFriendRequestParams
        ) { selectedUsers, error ->
            if (error != null) {
                Log.e(TAG, "친구 선택 실패", error)
            } else {
                Log.d(TAG, "친구 선택 성공 $selectedUsers")
                viewModel.updateGroupMemberList(mapSelectedUsersToMemberUIData(selectedUsers))
            }
        }
    }

    private fun mapSelectedUsersToMemberUIData(selectedUsers: SelectedUsers?) =
        selectedUsers?.users?.map { user ->
            MemberUIData(
                uuid = user.uuid,
                name = user.profileNickname ?: UNKNOWN_NICKNAME,
                profileImageUrl = user.profileThumbnailImage ?: DEFAULT_THUMBNAIL_URL
            )
        } ?: emptyList()

    companion object {
        private const val TAG = "CreateGroupActivity"
        private const val UNKNOWN_NICKNAME = "알 수 없음"
        private const val DEFAULT_THUMBNAIL_URL = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png"
        private val openPickerFriendRequestParams = OpenPickerFriendRequestParams(
            title = "멀티 피커", // 피커 이름
            viewAppearance = ViewAppearance.AUTO, // 피커 화면 모드
            orientation = PickerOrientation.AUTO, // 피커 화면 방향
            enableSearch = true, // 검색 기능 사용 여부
            enableIndex = true, // 인덱스뷰 사용 여부
            showFavorite = true, // 즐겨찾기 친구 표시 여부
            showPickedFriend = true, // 선택한 친구 표시 여부, 멀티 피커에만 사용 가능
            maxPickableCount = 100, // 선택 가능한 최대 대상 수
            minPickableCount = 1 // 선택 가능한 최소 대상 수
        )
    }
}
