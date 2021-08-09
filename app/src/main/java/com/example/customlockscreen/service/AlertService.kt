package com.example.customlockscreen.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.customlockscreen.R
import com.example.customlockscreen.activity.HomeActivity


const val NOTIFICATION_ID = 100

class AlertService : Service(){
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Thread{

            val manager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                val notificationChannel = NotificationChannel("chanel_1","normal",NotificationManager.IMPORTANCE_HIGH)
                manager.createNotificationChannel(notificationChannel)
            }


            val intent = Intent(this, HomeActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
            val build = NotificationCompat.Builder(this, "chanel_1")
                    .setContentTitle("Service")
                    .setContentText("send")
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
            Log.e("hhhhhh","1")

        }.start()

        return super.onStartCommand(intent, flags, startId)

    }

} 
