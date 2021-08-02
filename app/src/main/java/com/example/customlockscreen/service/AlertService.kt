package com.example.customlockscreen.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder

const val NOTIFICACTION_INTENT_CODE = 1

class AlertService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        val nm: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val n = Notification()

        nm.notify(NOTIFICACTION_INTENT_CODE,n)
    }

    fun getDefaultIntent(flags: Int):PendingIntent{
        val pendingIntent = PendingIntent.getActivity(this, NOTIFICACTION_INTENT_CODE,Intent(),flags)
        return pendingIntent
    }

}