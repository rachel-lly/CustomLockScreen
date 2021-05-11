package com.example.customlockscreen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customlockscreen.R
import com.example.customlockscreen.databinding.ActivityTimeRemindBinding



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




//        binding.todayEventTimeDate.setOnClickListener {
//
//            picker.show(this.supportFragmentManager,TODAY_EVENT_TIME_TAG)
//            picker.addOnPositiveButtonClickListener {
//                if(picker.hour<10){
//                    hour = "0${picker.hour}"
//                }
//                if(picker.minute<10){
//                    minute = "0${picker.minute}"
//                }
//                binding.todayEventTimeDate.text = "$hour:$minute"
//            }
//            picker.clearOnPositiveButtonClickListeners()
//
//        }





        setContentView(binding.root)
    }
}