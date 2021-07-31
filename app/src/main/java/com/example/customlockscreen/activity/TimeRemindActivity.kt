package com.example.customlockscreen.activity

import android.graphics.Color
import android.icu.number.IntegerWidth
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.customlockscreen.R
import com.example.customlockscreen.Util.SharedPreferenceCommission
import com.example.customlockscreen.databinding.ActivityTimeRemindBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat


class TimeRemindActivity : AppCompatActivity() {

    private lateinit var binding:ActivityTimeRemindBinding

    private var hour = "09"
    private var minute = "00"

    private var nowRemindTime = 0
    private var futureRemindTime = 0

    private val FUTURE_EVENT_TIME_TAG = "FUTURE_EVENT_TIME_TAG"
    private val TODAY_EVENT_TIME_TAG = "TODAY_EVENT_TIME_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityTimeRemindBinding.inflate(layoutInflater)

        setSupportActionBar(binding.detailToolbar)

        binding.detailToolbar.setNavigationIcon(R.mipmap.back)
        binding.detailToolbar.setNavigationOnClickListener {
            finish()
        }

        val isNowTimeRemind by SharedPreferenceCommission(this,"isNowTimeRemind",false)

        val isFutureTimeRemind by SharedPreferenceCommission(this,"isFutureTimeRemind",false)

        val nowRemind by SharedPreferenceCommission(this,"nowRemindTime",540)
        val futureRemind by SharedPreferenceCommission(this,"futureRemindTime",540)

        binding.todayEventTimeSwitch.isChecked = isNowTimeRemind
        binding.futureEventTimeSwitch.isChecked = isFutureTimeRemind

        var min = nowRemind%60
        var minStr = if(min<10) "0$min" else "$min"
        binding.todayEventTimeDate.text = "${nowRemind/60}:$minStr"

        min = futureRemind%60
        minStr = if(min<10) "0$min" else "$min"
        binding.futureEventTimeDate.text = "${futureRemind/60}:$minStr"


        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(9)
                .setMinute(0)
                .setTitleText("Select time")
                .build()

        picker.addOnPositiveButtonClickListener {

            hour = picker.hour.toString()

            if(picker.minute<10){
                minute = "0${picker.minute}"
            }else{
                minute = picker.minute.toString()
            }

            when(picker.tag){
                TODAY_EVENT_TIME_TAG ->{
                    binding.todayEventTimeDate.text = "$hour:$minute"
                    nowRemindTime = picker.hour*60 + picker.minute
                }

                FUTURE_EVENT_TIME_TAG ->{
                    binding.futureEventTimeDate.text = "$hour:$minute"
                    futureRemindTime = picker.hour*60 + picker.minute
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.activity_time_remind_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when(item.itemId){
            R.id.sure ->{

                var isNowTimeRemind by SharedPreferenceCommission(this,"isNowTimeRemind",false)

                var isFutureTimeRemind by SharedPreferenceCommission(this,"isFutureTimeRemind",false)

                isNowTimeRemind = binding.todayEventTimeSwitch.isChecked
                isFutureTimeRemind = binding.futureEventTimeSwitch.isChecked


                if(isNowTimeRemind){
                    var nowRemind by SharedPreferenceCommission(this,"nowRemindTime",540)
                    nowRemind = nowRemindTime
                }

                if (isFutureTimeRemind){
                    var futureRemind by SharedPreferenceCommission(this,"futureRemindTime",540)
                    futureRemind = futureRemindTime
                }

                finish()
            }
        }
        return true
    }
}