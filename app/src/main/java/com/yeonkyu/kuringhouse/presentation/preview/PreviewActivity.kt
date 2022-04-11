package com.yeonkyu.kuringhouse.presentation.preview

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
            itemClick = {
                // todo : start room activity
            }
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

        viewModel.dialogEvent.observe(this) {
            makeDialog(it)
        }
    }
}