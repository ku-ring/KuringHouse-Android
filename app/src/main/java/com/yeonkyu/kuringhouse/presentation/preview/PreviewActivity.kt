package com.yeonkyu.kuringhouse.presentation.preview

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
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

    private val requestAudioPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it == false) {
            makeDialog(getString(R.string.no_permission_description))
                .setOnConfirmClickLister { reRequestPermission() }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupView()
        setupListAdapter()
        observeData()

        if (!checkPermissionGranted()) {
            requestAudioPermission.launch(Manifest.permission.RECORD_AUDIO)
        }
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
            viewModel.fetchRoomList()
        }
    }

    private fun setupListAdapter() {
        roomAdapter = RoomAdapter(
            itemClick = {
                if (checkPermissionGranted()) {
                    startRoomActivity(id = it.id, title = it.title)
                } else {
                    requestAudioPermission.launch(Manifest.permission.RECORD_AUDIO)
                }
            }
        )

        binding.previewRecyclerview.apply {
            adapter = roomAdapter
            pager = RecyclerViewPager(
                recyclerView = this,
                isLoading = { viewModel.isLoading.value == true },
                loadNext = { viewModel.fetchRoomList() },
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

    private fun checkPermissionGranted(): Boolean {
        val audioPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        )
        return audioPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun reRequestPermission() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:$packageName")
        }
        startActivity(intent)
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
        viewModel.fetchRoomList()
    }
}