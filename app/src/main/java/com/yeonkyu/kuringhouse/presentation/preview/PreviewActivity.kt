package com.yeonkyu.kuringhouse.presentation.preview

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.sendbird.calls.*
import com.yeonkyu.kuringhouse.R
import com.yeonkyu.kuringhouse.databinding.ActivityPreviewBinding
import com.yeonkyu.kuringhouse.presentation.preview.bottomsheet.CreateRoomBottomSheet
import com.yeonkyu.kuringhouse.presentation.room.RoomActivity
import com.yeonkyu.kuringhouse.util.RecyclerViewPager
import com.yeonkyu.kuringhouse.util.makeDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviewBinding
    private val viewModel by viewModels<PreviewViewModel>()

    private lateinit var roomAdapter: RoomAdapter
    private lateinit var pager: RecyclerViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupView()
        setupListAdapter()
        observeData()
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

        binding.previewSwipeRefresh.setOnRefreshListener {
            pager.resetPage()
            viewModel.refreshRoomList()
            viewModel.isEnd.value = false
            viewModel.getRoomList()
        }
    }

    private fun setupListAdapter() {
        roomAdapter = RoomAdapter(
            itemClick = { startRoomActivity(id = it.id, title = it.title) }
        )

        binding.previewRecyclerview.apply {
            adapter = roomAdapter
            pager = RecyclerViewPager(
                recyclerView = this,
                isLoading = { viewModel.isLoading.value == true },
                loadNext = { viewModel.getRoomList() },
                isEnd = { viewModel.isEnd.value == true }
            )

            val dividerItemDecoration = DividerItemDecoration(this@PreviewActivity, LinearLayout.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }
    }

    private fun observeData() {
        viewModel.roomList.observe(this) {
            roomAdapter.submitList(it)
            binding.previewSwipeRefresh.isRefreshing = false
        }

        viewModel.createdRoomEvent.observe(this) {
            startRoomActivity(it.id, it.title)
        }

        viewModel.dialogEvent.observe(this) {
            makeDialog(getString(it))
        }
    }

    private fun startRoomActivity(id: String, title: String) {
        val intent = Intent(this, RoomActivity::class.java).apply {
            putExtra(RoomActivity.ROOM_ID, id)
            putExtra(RoomActivity.ROOM_TITLE, title)
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        roomAdapter.submitList(emptyList())
        viewModel.refreshRoomList()
        viewModel.getRoomList()
    }
}