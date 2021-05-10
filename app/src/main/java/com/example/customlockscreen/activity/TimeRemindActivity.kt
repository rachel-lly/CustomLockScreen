package com.example.customlockscreen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customlockscreen.R
import com.example.customlockscreen.databinding.ActivityTimeRemindBinding

class TimeRemindActivity : AppCompatActivity() {

    private lateinit var binding:ActivityTimeRemindBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTimeRemindBinding.inflate(layoutInflater)

        binding.detailToolbar.setNavigationIcon(R.mipmap.back)

        binding.detailToolbar.setNavigationOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }
}