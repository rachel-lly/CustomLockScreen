package com.example.customlockscreen.activity

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.example.customlockscreen.R
import com.example.customlockscreen.Util.CalendarReminderUtils
import com.example.customlockscreen.Util.SharedPreferenceCommission
import com.example.customlockscreen.databinding.ActivityTimeRemindBinding
import com.example.library.PermissionX
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat


class TimeRemindActivity : AppCompatActivity() {

    private lateinit var binding:ActivityTimeRemindBinding

    private lateinit var minute: String

    private var nowRemindTime = 0
    private var futureRemindTime = 0

    private val FUTURE_EVENT_TIME_TAG = "FUTURE_EVENT_TIME_TAG"
    private val TODAY_EVENT_TIME_TAG = "TODAY_EVENT_TIME_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityTimeRemindBinding.inflate(layoutInflater)

        binding.detailToolbar.setNavigationIcon(R.mipmap.back)
        binding.detailToolbar.setNavigationOnClickListener {
            finish()
        }

        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(this);
        val isEnabled = notificationManager.areNotificationsEnabled()

        var isNowTimeRemind by SharedPreferenceCommission(this, "isNowTimeRemind", false)

        var isFutureTimeRemind by SharedPreferenceCommission(this, "isFutureTimeRemind", false)

        if(!isEnabled){
            isNowTimeRemind = false
            isFutureTimeRemind = false
        }


        var nowRemind by SharedPreferenceCommission(this, "nowRemindTime", 540)
        var futureRemind by SharedPreferenceCommission(this, "futureRemindTime", 540)

        nowRemindTime = nowRemind
        futureRemindTime = futureRemind

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

            minute = if(picker.minute<10) "0${picker.minute}" else picker.minute.toString()

            when(picker.tag){
                TODAY_EVENT_TIME_TAG -> {
                    binding.todayEventTimeDate.text = "${picker.hour}:$minute"
                    nowRemindTime = picker.hour * 60 + picker.minute
                }

                FUTURE_EVENT_TIME_TAG -> {
                    binding.futureEventTimeDate.text = "${picker.hour}:$minute"
                    futureRemindTime = picker.hour * 60 + picker.minute
                }
            }
        }



        binding.todayEventTimeDate.setOnClickListener {
            picker.show(this.supportFragmentManager, TODAY_EVENT_TIME_TAG)
        }


        binding.futureEventTimeDate.setOnClickListener {
            picker.show(this.supportFragmentManager, FUTURE_EVENT_TIME_TAG)
        }

        binding.timeRemindSure.setOnClickListener {

            isNowTimeRemind = binding.todayEventTimeSwitch.isChecked
            isFutureTimeRemind = binding.futureEventTimeSwitch.isChecked


            if(isNowTimeRemind){
                nowRemind = nowRemindTime

            }

            if (isFutureTimeRemind){
                futureRemind = futureRemindTime

            }
        }

        setContentView(binding.root)
    }

    fun setRemind(title:String,description: String,startTime: Long,endTime: Long,previousDayRemind: Int){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {


            PermissionX.request(this,
                    Manifest.permission.WRITE_CALENDAR,
                    Manifest.permission.READ_CALENDAR){
                allGranted,deniedList ->
                run {
                    if (allGranted) {
                        CalendarReminderUtils.addCalendarEvent(this,title,description,startTime,endTime,previousDayRemind)
                    } else {
                        Toast.makeText(this,"你拒绝了 $deniedList", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

}