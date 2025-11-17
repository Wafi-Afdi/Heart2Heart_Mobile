package com.example.heart2heart.ECGExtraction.domain

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.heart2heart.ECGExtraction.model.ECGDataProcessingService
import com.example.heart2heart.R
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

@AndroidEntryPoint
class ObserverForegroundService: Service() {
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var notificationManager: NotificationManager

    private lateinit var wakeLock: PowerManager.WakeLock

    companion object {
        const val CHANNEL_ID = "EcgForegroundServiceChannel"
        const val NOTIFICATION_ID = 1

        const val ACTION_DISCONNECT = "com.heart2heart.socket.DISCONNECT"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!wakeLock.isHeld()) {
            acquireWakeLock()
        }
        val action = intent?.action
        when (action) {
            ECGForegroundService.Companion.ACTION_DISCONNECT -> {
                disconnect()
            }
        }

        val notification = createNotification()
        startForeground(ECGForegroundService.Companion.NOTIFICATION_ID, notification)

        return START_NOT_STICKY
    }

    private fun createNotification(): Notification {
        val disconnectPendingIntent = createDisconnectIntent()
        return NotificationCompat.Builder(this, ECGForegroundService.Companion.CHANNEL_ID)
            .setSmallIcon(R.drawable.sos_ic) // Make sure you have this icon
            .setContentTitle("Heart2Heart")
            .setContentText("Sedang dalam mode observer")
            .addAction(
                R.drawable.close_x,
                "Disconnect",
                disconnectPendingIntent
            )
            .setOngoing(true)
            .build()
    }

    private fun createDisconnectIntent(): PendingIntent {
        val intent = Intent(this, ObserverForegroundService::class.java).apply {
            action = ObserverForegroundService.Companion.ACTION_DISCONNECT // Attach the unique action
        }

        return PendingIntent.getService(
            this, // Context
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun disconnect() {
        serviceJob.cancel() // Clean up coroutines
        if (::wakeLock.isInitialized && wakeLock.isHeld) {
            wakeLock.release()
            Log.d("ECGForegroundService", "WakeLock released in onDestroy.")
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        acquireWakeLock()
        // createNotificationChannel()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        if (::wakeLock.isInitialized && wakeLock.isHeld) {
            wakeLock.release()
            Log.d("ECGForegroundService", "WakeLock released in onDestroy.")
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    @SuppressLint("WakelockTimeout")
    private fun acquireWakeLock() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "Heart2Heart::EcgServiceWakeLock"
        )
        wakeLock.acquire()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}