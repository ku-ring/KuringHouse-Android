package com.yeonkyu.kuringhouse.presentation.preview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.yeonkyu.kuringhouse.R
import com.yeonkyu.kuringhouse.databinding.ActivityPreviewBinding
import com.yeonkyu.kuringhouse.presentation.preview.bottomsheet.CreateRoomBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupView()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_preview)
        binding.lifecycleOwner = this
    }

    private fun setupView() {
        binding.previewCreateRoomBt.setOnClickListener {
            val bottomSheet = CreateRoomBottomSheet()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }
}