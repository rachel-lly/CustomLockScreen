package com.example.customlockscreen.service

import android.app.AlarmManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.customlockscreen.application.MyApplication
import com.example.customlockscreen.util.KvCommission
import com.example.customlockscreen.util.TimeManager

class AlertBootReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "android.intent.action.BOOT_COMPLETED") {
                // Set the alarm here.

                    val context =  MyApplication.getContext()

                val isNowTimeRemind by KvCommission("isNowTimeRemind", false)

                val isFutureTimeRemind by KvCommission("isFutureTimeRemind", false)

                val nowRemind by KvCommission( "nowRemindTime", 540)
                val futureRemind by KvCommission( "futureRemindTime", 540)





                val alarmManager = MyApplication.getContext().getSystemService(Service.ALARM_SERVICE) as AlarmManager


                val isAPIM = Build.VERSION.SDK_INT>= Build.VERSION_CODES.M


                if(isNowTimeRemind){
                    TimeManager().setTodayRemind(nowRemind,alarmManager,isAPIM,context)
                }

                if (isFutureTimeRemind){
                    TimeManager().setFutureRemind(futureRemind,alarmManager,isAPIM,context)
                }
            }
        }
    }
    