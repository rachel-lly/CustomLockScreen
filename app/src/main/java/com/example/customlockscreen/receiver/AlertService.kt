package com.example.customlockscreen.receiver

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.customlockscreen.R
import com.example.customlockscreen.activity.DetailActivity
import com.example.customlockscreen.activity.HomeActivity

class AlertService : Service(){
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Thread{


                val manager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val intent = Intent(this, HomeActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(this, 0,intent,0)
                val notification = NotificationCompat.Builder(this,"normal")
                        .setContentTitle("Service")
                        .setContentText("send")
                        .setSmallIcon(R.mipmap.top_icon)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build()
                manager.notify(NOTIFICATION_ID,notification)




        }.start()

        return super.onStartCommand(intent, flags, startId)
    }

}