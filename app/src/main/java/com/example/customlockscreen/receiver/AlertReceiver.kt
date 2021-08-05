package com.example.customlockscreen.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.example.customlockscreen.R
import com.example.customlockscreen.activity.DetailActivity
import com.example.customlockscreen.activity.HomeActivity

const val NOTIFICATION_ID = 1000

class AlertReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if(intent?.action.equals("NOTIFICATION")){

            val manager: NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val intent = Intent(context, HomeActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0,intent,0)

            val notification = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                NotificationCompat.Builder(context,"normal")
                        .setContentTitle("日程提醒")
                        .setContentText("距离开学还有 24 天")
                        .setSmallIcon(R.mipmap.top_icon)
                        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.avater))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build()
            } else {
                NotificationCompat.Builder(context,"normal")
                        .setContentTitle("日程提醒")
                        .setContentText("距离开学还有 24 天")
                        .setSmallIcon(R.mipmap.top_icon)
                        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.avater))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build()
            }

            manager.notify(NOTIFICATION_ID,notification)


        }

    }

}