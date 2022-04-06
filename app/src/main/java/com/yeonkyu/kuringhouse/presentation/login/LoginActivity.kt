package com.yeonkyu.kuringhouse.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sendbird.calls.AuthenticateParams
import com.sendbird.calls.SendBirdCall
import com.yeonkyu.kuringhouse.R
import com.yeonkyu.kuringhouse.databinding.ActivityLoginBinding
import com.yeonkyu.kuringhouse.presentation.preview.PreviewActivity
import com.yeonkyu.kuringhouse.util.makeDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()

        binding.loginAuthorizeBt.setOnClickListener {
            val email = binding.loginEmailEt.text.toString()
            val accessToken = binding.loginInvitationCodeEt.text.toString()
            val params = AuthenticateParams(userId = email)
                .setAccessToken(accessToken = accessToken)

            if(email.isEmpty()) {
                makeDialog("이메일을 입력해주세요")
                return@setOnClickListener
            }
            if (accessToken.isEmpty()) {
                makeDialog("초대 코드를 입력해주세요")
                return@setOnClickListener
            }

            SendBirdCall.authenticate(params = params) { user, e ->
                if (e == null) {
                    Timber.e("SB auth success. id : ${user?.userId}")
                    viewModel.saveUser(email, accessToken)
                    startPreviewActivity()
                } else {
                    Timber.e("SB auth fail : [${e.code}] ${e.message}")
                    makeDialog("로그인 실패", e.message)
                }
            }
        }
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
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