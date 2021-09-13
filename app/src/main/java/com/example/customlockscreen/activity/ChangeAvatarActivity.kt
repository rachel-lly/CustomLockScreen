package com.example.customlockscreen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customlockscreen.R
import com.example.customlockscreen.databinding.ActivityChangeAvatarBinding

class ChangeAvatarActivity : AppCompatActivity() {

    lateinit var binding: ActivityChangeAvatarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangeAvatarBinding.inflate(layoutInflater)

        setSupportActionBar(binding.changeAvatarToolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.changeAvatarToolbar.setNavigationIcon(R.mipmap.back)
        binding.changeAvatarToolbar.setNavigationOnClickListener {
            finish()
        }



        setContentView(binding.root)
    }
}