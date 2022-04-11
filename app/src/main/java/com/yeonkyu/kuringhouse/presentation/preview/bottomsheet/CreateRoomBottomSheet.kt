package com.yeonkyu.kuringhouse.presentation.preview.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeonkyu.kuringhouse.R
import com.yeonkyu.kuringhouse.databinding.BottomSheetCreateRoomBinding
import com.yeonkyu.kuringhouse.presentation.preview.PreviewViewModel

class CreateRoomBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetCreateRoomBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<PreviewViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetCreateRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeData()
    }

    private fun setupView() {
        binding.createRoomStart.setOnClickListener {
            val title = binding.createRoomName.text.toString()
            viewModel.createRoom(title)
        }
    }

    private fun observeData() {
        viewModel.dismissBottomSheetEvent.observe(viewLifecycleOwner) {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}