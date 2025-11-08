package com.example.heart2heart

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.heart2heart.ECGExtraction.domain.ECGForegroundService.Companion.CHANNEL_ID
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Heart2HeartApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "EcgForegroundServiceChannel",
                "ECG Monitoring Service",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(serviceChannel)
        }
    }
}