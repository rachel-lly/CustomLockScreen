package com.example.customlockscreen.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.customlockscreen.R
import com.example.customlockscreen.activity.DetailActivity

class AlertReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val manager: NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val id = intent?.getStringExtra("id")


        //点击通知跳转的 activity
        val intent = Intent(context,DetailActivity::class.java)
        intent.putExtra("id",id)

        val pendingIntent = PendingIntent.getActivity(context, NOTIFICACTION_INTENT_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context,"default")
        builder.setContentTitle("title")
                .setContentText("text")
                .setSmallIcon(R.drawable.avater)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSubText("二级text")

        manager.notify(NOTIFICACTION_INTENT_CODE,builder.build())


    }

}