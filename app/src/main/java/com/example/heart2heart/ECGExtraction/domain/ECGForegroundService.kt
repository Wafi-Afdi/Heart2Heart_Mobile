package com.example.heart2heart.ECGExtraction.domain

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.heart2heart.R
import com.example.heart2heart.bluetooth.BluetoothServiceECG
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ECGForegroundService: Service() {

    @Inject
    lateinit var bluetoothController: BluetoothServiceECG

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var notificationManager: NotificationManager

    companion object {
        const val CHANNEL_ID = "EcgForegroundServiceChannel"
        const val NOTIFICATION_ID = 1

        const val ACTION_DISCONNECT = "com.heart2heart.bluetooth.DISCONNECT"
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        when (action) {
            ACTION_DISCONNECT -> {
                disconnect()
            }
        }
        val notification = createNotification()

        bluetoothController.incomingSamples
            .onEach { sample ->
                //Log.w("ECGForegroundService", "ECG sample: $sample")
                // Optionally store to DB or send to server here
            }
            .launchIn(serviceScope)

        bluetoothController.isConnected
            .onEach { isConnected ->
                if (isConnected) {
                    // Update notification if needed
                    val newNotification = createNotification()
                    notificationManager.notify(NOTIFICATION_ID, newNotification)
                } else {
                    // Connection lost, stop the service
                    stopSelf()
                }
            }
            .launchIn(serviceScope)

        startForeground(NOTIFICATION_ID, notification)

        return START_NOT_STICKY
    }

    private fun disconnect() {
        Log.i("BluetoothService", "Disconnecting Bluetooth...")
        bluetoothController.closeConnection()
        serviceJob.cancel() // Clean up coroutines
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotification(): Notification {
        val disconnectPendingIntent = createDisconnectIntent()
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.sos_ic) // Make sure you have this icon
            .setContentTitle("Heart2Heart")
            .setContentText("ECG sedang jalan")
            .addAction(
                R.drawable.setting_ic,
                "Disconnect",
                disconnectPendingIntent
            )
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "ECG Monitoring Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(serviceChannel)
        }
    }

    private fun createDisconnectIntent(): PendingIntent {
        val intent = Intent(this, ECGForegroundService::class.java).apply {
            action = ACTION_DISCONNECT // Attach the unique action
        }

        return PendingIntent.getService(
            this, // Context
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothController.closeConnection()
        serviceJob.cancel() // Clean up coroutines
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}