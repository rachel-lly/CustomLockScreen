package com.example.customlockscreen.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.customlockscreen.R
import com.example.customlockscreen.activity.DetailActivity
import com.example.customlockscreen.activity.LABEL
import com.example.customlockscreen.model.db.DataBase
import com.example.customlockscreen.util.LABEL_TEXT


const val NOTIFICATION_ID = 100

class AlertService : Service(){
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Thread{

            val labelText = intent!!.getStringExtra(LABEL_TEXT)

            val label = DataBase.dataBase.labelDao().getLabelByName(labelText!!)

            if(label!=null){
                val manager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                    val notificationChannel = NotificationChannel("chanel_1","normal",NotificationManager.IMPORTANCE_HIGH)
                    manager.createNotificationChannel(notificationChannel)
                }


                val toIntent = Intent(this, DetailActivity::class.java)
                toIntent.putExtra(LABEL,label)


                val pendingIntent = PendingIntent.getActivity(this, 0, toIntent, 0)

                val build = NotificationCompat.Builder(this, "chanel_1")
                        .setContentTitle("${label.text}")
                        .setContentText("还有 ${label.day} 天")
                        .setSmallIcon(R.mipmap.top_icon)
                        .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.avater))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    build.priority = NotificationManager.IMPORTANCE_HIGH
                }else{
                    build.priority = Notification.PRIORITY_HIGH
                }
                manager.notify(NOTIFICATION_ID, build.build())
            }



        }.start()

        return super.onStartCommand(intent, flags, startId)

    }

} 
