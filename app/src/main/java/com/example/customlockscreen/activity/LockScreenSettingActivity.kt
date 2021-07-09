package com.example.customlockscreen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        binding = ActivityLockScreenSettingBinding.inflate(layoutInflater)

        binding.lockScreenSettingToolbar.setNavigationIcon(R.mipmap.back)
        binding.lockScreenSettingToolbar.setNavigationOnClickListener {
            finish()
        }



        labelList = ArrayList<Label>()
        labelLinearAdapter = this.let { LabelLinearAdapter(it, labelList,true) }

        binding.lockScreenSettingRecyclerview.adapter = labelLinearAdapter
        binding.lockScreenSettingRecyclerview.layoutManager = GridLayoutManager(this, 1)

        setContentView(binding.root)
    }
}