package com.yeonkyu.kuringhouse.presentation.room

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.yeonkyu.kuringhouse.R
import com.yeonkyu.kuringhouse.databinding.ActivityRoomBinding
import com.yeonkyu.kuringhouse.util.makeDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoomBinding
    private val viewModel by viewModels<RoomViewModel>()

    private lateinit var memberAdapter: MemberAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupView()
        setupListAdapter()
        observeData()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_room)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setupView() {
        viewModel.roomName.postValue(intent.getStringExtra(ROOM_TITLE) ?: "")

        viewModel.roomId = intent.getStringExtra(ROOM_ID) ?: ""
        if (viewModel.roomId.isEmpty()) {
            makeDialog(getString(R.string.room_info_fail_try_again))
                .setOnDismissListener { finish() }
        }
        else {
            viewModel.getRoom(viewModel.roomId)
        }

        binding.roomMic.setOnClickListener {
            viewModel.switchMic()
        }
    }

    private fun setupListAdapter() {
        memberAdapter = MemberAdapter()

        binding.recyclerview.apply {
            adapter = memberAdapter
            layoutManager = GridLayoutManager(this@RoomActivity, 3)
        }
    }

    private fun observeData() {
        viewModel.activeMemberList.observe(this) {
            memberAdapter.submitList(it)
        }

        viewModel.dialogEvent.observe(this) {
            makeDialog(getString(it))
        }

        viewModel.isMicOn.observe(this) {
            if (it == true) {
                binding.roomMic.setImageResource(R.drawable.ic_mic_on)
            } else {
                binding.roomMic.setImageResource(R.drawable.ic_mic_off)
            }
        }
    }

    companion object {
        const val ROOM_ID = "ROOM_ID"
        const val ROOM_TITLE = "ROOM_TITLE"
    }
}