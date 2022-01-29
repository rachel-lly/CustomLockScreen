package com.example.customlockscreen.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.customlockscreen.R
import com.example.customlockscreen.adapter.LabelLinearAdapter
import com.example.customlockscreen.databinding.ActivityLockScreenSettingBinding
import com.example.customlockscreen.model.bean.Label
import com.example.customlockscreen.model.db.DataBase


class LockScreenSettingActivity : AppCompatActivity() {


    private lateinit var labelList :List<Label>

    private val labelDao = DataBase.dataBase.labelDao()


    private lateinit var binding:ActivityLockScreenSettingBinding

    private lateinit var labelLinearAdapter: LabelLinearAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =  DataBindingUtil.setContentView(this,R.layout.activity_lock_screen_setting)

        binding.lifecycleOwner = this

        val slide = Slide()
        slide.slideEdge = Gravity.LEFT
        slide.excludeTarget(android.R.id.statusBarBackground, true)
        window.exitTransition = slide


        val slide2 = Slide()
        slide2.slideEdge = Gravity.RIGHT
        slide2.excludeTarget(android.R.id.statusBarBackground, true)
        window.enterTransition = slide2

        binding.lockScreenSettingToolbar.setNavigationIcon(R.mipmap.back)
        binding.lockScreenSettingToolbar.setNavigationOnClickListener {
            finishAfterTransition()
        }


        labelList = labelDao.getAllLabels()
        labelLinearAdapter = LabelLinearAdapter(this, labelList,true)

        binding.lockScreenSettingRecyclerview.adapter = labelLinearAdapter
        binding.lockScreenSettingRecyclerview.layoutManager = GridLayoutManager(this, 1)

    }
}