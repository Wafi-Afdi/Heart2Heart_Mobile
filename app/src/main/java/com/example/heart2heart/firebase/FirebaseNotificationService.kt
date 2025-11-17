package com.example.heart2heart.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.heart2heart.ECGExtraction.data.DetectionType
import com.example.heart2heart.EmergencyBroadcast.domain.EmergencyBroadcastService
import com.example.heart2heart.MainActivity
import com.example.heart2heart.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class FirebaseNotificationService : FirebaseMessagingService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    @Inject
    lateinit var emergencyBroadcastService: EmergencyBroadcastService

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Send token to your server
        println("FCM Token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.notification?.let {
            sendNotification(it.title, it.body)
        }

        if (message.data.isNotEmpty() && message.messageType == "report") {
            Log.i("FirebaseNotif", message.data.toString())
            handleDataPayload(message.data)
        }
    }

    private fun sendNotification(title: String?, body: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "heart2heart_fcm"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.heart2heart_logo_black)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun handleDataPayload(data: Map<String, String>) {
        // Process custom data
        val title = data["title"]
        val body = data["body"]
        val userId = data["userId"]
        val name = data["name"]
        val reportType = data["report"]
        serviceScope.launch {

            if (reportType == "SOS" && name != null) {
                emergencyBroadcastService.emitSOSObserver(name)
            } else if (reportType == "VFib"  && name != null) {
                emergencyBroadcastService.emitReport(name, DetectionType.VFIB)
            } else if (reportType == "VT"  && name != null) {
                emergencyBroadcastService.emitReport(name, DetectionType.VT)
            } else if (reportType == "AFib"  && name != null) {
                emergencyBroadcastService.emitReport(name, DetectionType.AFIB)
            } else if (reportType == "Asystole"  && name != null) {
                emergencyBroadcastService.emitReport(name, DetectionType.ASYSTOLE)
            } else if (reportType == "Tachycardia"  && name != null) {
                emergencyBroadcastService.emitReport(name, DetectionType.TACHYCARDHIA)
            } else if (reportType == "Bradycardia"  && name != null) {
                emergencyBroadcastService.emitReport(name, DetectionType.BRADYCARDHIA)
            } else if (reportType == "Normal"  && name != null) {
                emergencyBroadcastService.emitReport(name, DetectionType.NORMAL)
            }
        }
    }
}