package com.example.customlockscreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.customlockscreen.databinding.ActivityAddNoteBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import javax.security.auth.callback.Callback
import kotlin.time.ExperimentalTime

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddNoteBinding

    private val END_TIME_TAG = "AddNoteActivity-endTime"
    private val ADD_NOTE_TIME_TAG = "AddNoteActivity-addNoteTime"

    private var isFirst = true

    @SuppressLint("SimpleDateFormat")
    private val format = SimpleDateFormat("yyyy-MM-dd-EE")

    private val today = format.format(MaterialDatePicker.todayInUtcMilliseconds())

    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)

        var bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)


        val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()


        binding.endTimeDate.visibility = View.GONE

        setSupportActionBar(binding.addNoteToolbar)

        binding.addNoteToolbar.setNavigationIcon(R.mipmap.back)

        binding.addNoteToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.endTimeSwitch.setOnClickListener {

            if(isFirst){
                binding.endTimeDate.visibility = View.VISIBLE
                isFirst = false
            }else{
                binding.endTimeDate.visibility = View.GONE
                isFirst = true
            }

        }

        // TODO: 2021/4/19 获取选择的时间 
        binding.endTimeDate.setOnClickListener {
           datePicker.show(this.supportFragmentManager,END_TIME_TAG)
            datePicker.addOnPositiveButtonClickListener {
                val chooseDay = format.format(it)
                binding.endTimeDate.text = chooseDay
                if(chooseDay<today){
                    binding.endTimeDate.setTextColor(ContextCompat.getColor(this,R.color.color_passed))
                }else{
                    binding.endTimeDate.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))
                }
            }
        }

        binding.addNoteDate.setOnClickListener {
            datePicker.show(this.supportFragmentManager,ADD_NOTE_TIME_TAG)
            datePicker.addOnPositiveButtonClickListener {
                val chooseDay = format.format(it)
                binding.addNoteDate.text = chooseDay
                if(chooseDay<today){
                    binding.addNoteDate.setTextColor(ContextCompat.getColor(this,R.color.color_passed))
                }else{
                    binding.addNoteDate.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))
                }


            }
        }

        // TODO: 2021/4/19 分类事件的点击：弹出弹窗几个分类，新增分类跳转activity 
        binding.chooseSortNoteLayout.setOnClickListener {

            if(bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
            }else{
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            }

        }

        // TODO: 2021/4/21 选择分类
        binding.sortLifeLayout.setOnClickListener {
            binding.chooseSortTv.text = "生活"
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }

        binding.sortWorkLayout.setOnClickListener {
            binding.chooseSortTv.text = "工作"
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }

        binding.sortAnniversaryLayout.setOnClickListener {
            binding.chooseSortTv.text = "纪念日"
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }

        setContentView(binding.root)
    }
}