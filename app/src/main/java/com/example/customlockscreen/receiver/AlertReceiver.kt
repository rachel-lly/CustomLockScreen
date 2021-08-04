package com.example.customlockscreen.receiver

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.customlockscreen.R
import com.example.customlockscreen.activity.DetailActivity

const val NOTIFICATION_ID = 1000

class AlertReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if(intent?.action.equals("NOTIFICATION")){

            val manager: NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val intent = Intent(context,DetailActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0,intent,0)
            val notification = NotificationCompat.Builder(context,"normal")
                    .setContentTitle("日程提醒")
                    .setContentText("距离开学还有 24 天")
                    .setSmallIcon(R.mipmap.top_icon)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()
            manager.notify(NOTIFICATION_ID,notification)


        }

    }

}