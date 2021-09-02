package com.example.customlockscreen.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.example.customlockscreen.R
import com.example.customlockscreen.util.SharedPreferenceCommission
import com.example.customlockscreen.databinding.ActivityTimeRemindBinding
import com.example.customlockscreen.service.AlertService
import com.example.customlockscreen.service.AlertBootReceiver
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*


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

            if(isNowTimeRemind || isFutureTimeRemind){
                // TODO: 2021/8/6 服务发送通知
                val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(this)
                val isEnabled = notificationManager.areNotificationsEnabled()
                if(!isEnabled){
                    MaterialAlertDialogBuilder(this)
                            .setTitle("提示")
                            .setMessage("是否允许应用打开通知权限？")
                            .setNegativeButton(resources.getString(R.string.decline)){dialog,which ->
                                Toast.makeText(this,"拒绝接收通知", Toast.LENGTH_SHORT).show()
                                dialog.cancel()
                            }
                            .setPositiveButton(resources.getString(R.string.accept)){ dialog,which ->
                                dialog.cancel()
                                val intent = Intent()
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                                    intent.putExtra("android.provider.extra.APP_PACKAGE", packageName)
                                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //5.0
                                    intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                                    intent.putExtra("app_package", packageName)
                                    intent.putExtra("app_uid", applicationInfo.uid)
                                    startActivity(intent)
                                } else {
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                                    intent.data = Uri.fromParts("package", packageName, null)
                                }
                                startActivity(intent)
                            }
                            .show()
                }

                //开启通知后，启动开机自启
                val receiver = ComponentName(this, AlertBootReceiver::class.java)

                packageManager.setComponentEnabledSetting(
                        receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                )

                val alarmManager = getSystemService(Service.ALARM_SERVICE) as AlarmManager
                val alarmIntent = Intent(this, AlertService::class.java)
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"))


                //若设置多个定时任务 requestCode要设置多个 唯一性

                val pendingIntent = PendingIntent.getService(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                alarmManager.cancel(pendingIntent)

//                calendar.set(year, month, day, hour, 0)


                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    //版本大于Android 6.0
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                }else{
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                }

                if(isNowTimeRemind){
                    nowRemind = nowRemindTime

                }

                if (isFutureTimeRemind){
                    futureRemind = futureRemindTime

                }
            }else{
                //取消开机自启
                val receiver = ComponentName(this, AlertBootReceiver::class.java)
                packageManager.setComponentEnabledSetting(
                        receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                )
            }
        }

        setContentView(binding.root)
    }

}