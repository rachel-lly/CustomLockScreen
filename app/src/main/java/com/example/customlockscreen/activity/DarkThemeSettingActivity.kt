package com.example.customlockscreen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import com.example.customlockscreen.R
import com.example.customlockscreen.databinding.ActivityDarkThemeSettingBinding

class DarkThemeSettingActivity : AppCompatActivity() {

    private lateinit var binding:ActivityDarkThemeSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDarkThemeSettingBinding.inflate(LayoutInflater.from(this))


        val slide = Slide()
        slide.slideEdge = Gravity.LEFT
        slide.excludeTarget(android.R.id.statusBarBackground, true)
        window.exitTransition = slide


        val slide2 = Slide()
        slide2.slideEdge = Gravity.RIGHT
        slide2.excludeTarget(android.R.id.statusBarBackground, true)
        window.enterTransition = slide2

        binding.darkThemeSettingToolbar.setNavigationIcon(R.mipmap.back)
        binding.darkThemeSettingToolbar.setNavigationOnClickListener {
            finishAfterTransition()
        }


        setContentView(binding.root)
    }
}