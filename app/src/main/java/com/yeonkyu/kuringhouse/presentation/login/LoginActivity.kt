package com.yeonkyu.kuringhouse.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.yeonkyu.kuringhouse.R
import com.yeonkyu.kuringhouse.databinding.ActivityLoginBinding
import com.yeonkyu.kuringhouse.presentation.preview.PreviewActivity
import com.yeonkyu.kuringhouse.util.makeDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        observeEvent()

        binding.loginAuthorizeBt.setOnClickListener {
            viewModel.authorize()
        }
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun observeEvent() {
        viewModel.dialogEvent.observe(this) {
            makeDialog(it.first, it.second)
        }
        viewModel.authSuccess.observe(this) {
            startPreviewActivity()
        }
    }

    private fun startPreviewActivity() {
        val intent = Intent(this, PreviewActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0)
    }
}