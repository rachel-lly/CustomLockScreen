package com.example.customlockscreen.util

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.customlockscreen.model.bean.Label
import com.example.customlockscreen.model.db.DataBase
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
            for(i in 0 until allLabel.size){
                val refreshLabel = allLabel.get(i)

                if(refreshLabel.endDate<=nowTime){

                    labelDao.deleteLabel(refreshLabel)

                }else{

                    refreshLabel.day =  refreshLabel.targetDate/(1000*3600*24)-nowTime/(1000*3600*24)
                    labelDao.updateLabel(refreshLabel)

                }

            }
        }
    }

    fun getTodayRemindList():List<Label>{
        refreshRoomLabelListDay()
        val list = labelDao.getAllLabels()

        val res = ArrayList<Label>()

        for(label in list){
            if(label.day in 0..3){//5天之内加入提醒
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
            if(label.day in 1..6){//5天之内加入提醒
                res.add(label)
            }
        }
        return res
    }


    fun setTodayRemind(remindTime:Int, alarmManager: AlarmManager, isLargerM: Boolean,context: Context,alarmIntent: Intent){
        setRemind(remindTime,alarmManager,isLargerM,context,alarmIntent,getTodayRemindList())
    }

    fun setFutureRemind(remindTime:Int, alarmManager: AlarmManager, isLargerM: Boolean,context: Context,alarmIntent: Intent){
        setRemind(remindTime,alarmManager,isLargerM,context,alarmIntent,getFutureRemindList())
    }

    @SuppressLint("NewApi")
    fun setRemind(remindTime:Int, alarmManager: AlarmManager, isLargerM: Boolean,context: Context,alarmIntent: Intent,remindList: List<Label>){

        val hour = remindTime/60
        val min = remindTime%60

        val calendar:Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"))

        if(isLargerM){
            for(label in remindList){
                //若设置多个定时任务 requestCode要设置多个 唯一性
                val pendingIntent = PendingIntent.getService(context, label.id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                alarmManager.cancel(pendingIntent)

                calendar.time = Date(label.targetDate)
                Log.e("(>=M)Time","${label.targetDate}----->day:${calendar.get(Calendar.DAY_OF_MONTH)}--hour:${calendar.get(Calendar.HOUR)}--min::${calendar.get(Calendar.MINUTE)}")
                calendar.set(Calendar.HOUR,hour)
                calendar.set(Calendar.MINUTE,min)
                Log.e("(>=M)TimeChange","${label.targetDate}----->day:${calendar.get(Calendar.DAY_OF_MONTH)}--hour:${calendar.get(Calendar.HOUR)}--min::${calendar.get(Calendar.MINUTE)}")
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        }else{
            for(label in remindList){

                val pendingIntent = PendingIntent.getService(context, label.id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                alarmManager.cancel(pendingIntent)

                calendar.time = Date(label.targetDate)
                Log.e("Time","${label.targetDate}----->day:${calendar.get(Calendar.DAY_OF_MONTH)}--hour:${calendar.get(Calendar.HOUR)}--min::${calendar.get(Calendar.MINUTE)}")
                calendar.set(Calendar.HOUR,hour)
                calendar.set(Calendar.MINUTE,min)
                Log.e("Time","${label.targetDate}----->day:${calendar.get(Calendar.DAY_OF_MONTH)}--hour:${calendar.get(Calendar.HOUR)}--min::${calendar.get(Calendar.MINUTE)}")
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        }

    }

}