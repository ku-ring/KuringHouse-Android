package com.yeonkyu.kuringhouse.presentation.preview

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sendbird.calls.*
import com.sendbird.calls.handler.RoomListQueryResultHandler
import com.yeonkyu.kuringhouse.R
import com.yeonkyu.kuringhouse.databinding.ActivityPreviewBinding
import com.yeonkyu.kuringhouse.presentation.preview.bottomsheet.CreateRoomBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviewBinding
    private val viewModel by viewModels<PreviewViewModel>()
    private lateinit var roomAdapter: RoomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupView()
        setupListAdapter()
        observeData()

        viewModel.getRoomList()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_preview)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setupView() {
        binding.previewCreateRoomBt.bringToFront()

        binding.previewCreateRoomBt.setOnClickListener {
            val bottomSheet = CreateRoomBottomSheet()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }

    private fun setupListAdapter() {
        roomAdapter = RoomAdapter(
            itemClick = {
                // todo : start room activity
            }
        )

        binding.previewRecyclerview.apply {
            adapter = roomAdapter
        }
    }

    private fun observeData() {
        viewModel.roomList.observe(this) {
            roomAdapter.submitList(it)
        }
    }
}