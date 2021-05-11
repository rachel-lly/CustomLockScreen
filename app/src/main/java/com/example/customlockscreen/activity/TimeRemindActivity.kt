package com.example.customlockscreen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.customlockscreen.R
import com.example.customlockscreen.databinding.ActivityTimeRemindBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat


class TimeRemindActivity : AppCompatActivity() {

    private lateinit var binding:ActivityTimeRemindBinding

    private var hour = "09"
    private var minute = "00"

    private val FUTURE_EVENT_TIME_TAG = "FUTURE_EVENT_TIME_TAG"
    private val TODAY_EVENT_TIME_TAG = "TODAY_EVENT_TIME_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTimeRemindBinding.inflate(layoutInflater)

        binding.detailToolbar.setNavigationIcon(R.mipmap.back)

        binding.detailToolbar.setNavigationOnClickListener {
            finish()
        }


        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(9)
                .setMinute(0)
                .setTitleText("Select time")
                .build()

        picker.addOnPositiveButtonClickListener {

            if(picker.hour<10){
                hour = "0${picker.hour}"
            }else{
                hour = picker.hour.toString()
            }
            if(picker.minute<10){
                minute = "0${picker.minute}"
            }else{
                minute = picker.minute.toString()
            }

            when(picker.tag){
                TODAY_EVENT_TIME_TAG ->{
                    binding.todayEventTimeDate.text = "$hour:$minute"
                }

                FUTURE_EVENT_TIME_TAG ->{
                    binding.futureEventTimeDate.text = "$hour:$minute"
                }
            }
        }



        binding.todayEventTimeDate.setOnClickListener {
            picker.show(this.supportFragmentManager,TODAY_EVENT_TIME_TAG)
        }


        binding.futureEventTimeDate.setOnClickListener {
            picker.show(this.supportFragmentManager,FUTURE_EVENT_TIME_TAG)
        }




        setContentView(binding.root)
    }
}