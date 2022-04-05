package com.yeonkyu.kuringhouse.presentation.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yeonkyu.kuringhouse.databinding.ActivityOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}