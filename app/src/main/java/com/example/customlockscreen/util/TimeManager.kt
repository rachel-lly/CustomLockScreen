package com.example.customlockscreen.util

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.customlockscreen.model.bean.Label
import com.example.customlockscreen.model.db.DataBase
import com.example.customlockscreen.service.AlertService
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList



class TimeManager {

    companion object{

        val format = SimpleDateFormat("yyyy-MM-dd-EE", Locale.CHINESE)

        val labelDao  = DataBase.dataBase.labelDao()

        fun refreshRoomLabelListDay(){
            val allLabel : List<Label> = labelDao.getAllLabels()
            val nowTime: Long = System.currentTimeMillis()
            for(i in allLabel.indices){
                val refreshLabel = allLabel[i]

                if(refreshLabel.endDate<=nowTime){

                    labelDao.deleteLabel(refreshLabel)

                }else{

                    refreshLabel.day =  refreshLabel.targetDate/(1000*3600*24)-nowTime/(1000*3600*24)
                    labelDao.updateLabel(refreshLabel)

                }

            }
        }
    }

    private fun getTodayRemindList():List<Label>{
        refreshRoomLabelListDay()
        val list = labelDao.getAllLabels()

        val res = ArrayList<Label>()

        for(label in list){
            if(label.day in 0..3){//3天之内加入提醒
                res.add(label)
            }
        }
        return res
    }

    fun getFutureRemindList():List<Label>{
        refreshRoomLabelListDay()
        val list = labelDao.getAllLabels()

        val res = ArrayList<Label>()

        for(label in list){
            if(label.day in 3..6){//提前3天加入提醒
                res.add(label)
            }
        }
        return res
    }


    fun setTodayRemind(remindTime: Int, alarmManager: AlarmManager, isLargerM: Boolean, context: Context){
        setRemind(remindTime, alarmManager, isLargerM, context, getTodayRemindList(), false)
    }

    fun setFutureRemind(remindTime: Int, alarmManager: AlarmManager, isLargerM: Boolean, context: Context){
        setRemind(remindTime, alarmManager, isLargerM, context, getFutureRemindList(), true)
    }

    @SuppressLint("NewApi")
    fun setRemind(remindTime: Int, alarmManager: AlarmManager, isLargerM: Boolean, context: Context, remindList: List<Label>, isFuture: Boolean){

        val hour = remindTime/60
        val min = remindTime%60


        val alarmIntent = Intent(context, AlertService::class.java)
        val calendar:Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"))

        if(isLargerM){
            for(label in remindList){
                //若设置多个定时任务 requestCode要设置多个 唯一性
               alarmIntent.putExtra(Code.LABEL_TEXT, label.text)
                alarmIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                val pendingIntent = PendingIntent.getService(context, label.id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                Log.e("Time", "${label.text}--${label.id}")

                calendar.time = Date(label.targetDate)

               if(isFuture){
                    calendar.add(Calendar.DAY_OF_MONTH, -3)
                }

                calendar.set(Calendar.HOUR, hour)
                calendar.set(Calendar.MINUTE, min)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        }else{
            for(label in remindList){

                alarmIntent.putExtra(Code.LABEL_TEXT, label.text)
                alarmIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                val pendingIntent = PendingIntent.getService(context, label.id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)

                calendar.time = Date(label.targetDate)

                if(isFuture){
                    calendar.add(Calendar.DAY_OF_MONTH, -3)
                }

                calendar.set(Calendar.HOUR, hour)
                calendar.set(Calendar.MINUTE, min)
               alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        }

    }

}