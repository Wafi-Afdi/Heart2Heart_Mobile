package com.example.heart2heart.ECGExtraction.domain

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.heart2heart.ECGExtraction.data.DetectionType
import com.example.heart2heart.ECGExtraction.data.ReportType
import com.example.heart2heart.ECGExtraction.model.ECGDataProcessingService
import com.example.heart2heart.EmergencyBroadcast.data.repository.ReportRepository
import com.example.heart2heart.EmergencyBroadcast.domain.EmergencyBroadcastService
import com.example.heart2heart.EmergencyBroadcast.domain.data.EmergencyEvent
import com.example.heart2heart.R
import com.example.heart2heart.bluetooth.BluetoothServiceECG
import com.example.heart2heart.home.domain.LiveLocationService
import com.example.heart2heart.websocket.repository.WebSocketRepository
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime

@AndroidEntryPoint
class ECGForegroundService: Service() {

    @Inject
    lateinit var bluetoothController: BluetoothServiceECG

    @Inject
    lateinit var locationService: LiveLocationService

    @Inject
    lateinit var ecgDataProcessingService: ECGDataProcessingService

    @Inject
    lateinit var websocketAmbulatory: WebSocketRepository

    @Inject
    lateinit var ecgEmergencyBroadcastService: EmergencyBroadcastService

    @Inject
    lateinit var reportRepository: ReportRepository

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var notificationManager: NotificationManager

    private lateinit var wakeLock: PowerManager.WakeLock

    companion object {
        const val CHANNEL_ID = "EcgForegroundServiceChannel"
        const val NOTIFICATION_ID = 1

        const val ACTION_DISCONNECT = "com.heart2heart.bluetooth.DISCONNECT"
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        acquireWakeLock()
        // createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!wakeLock.isHeld()) {
            acquireWakeLock()
        }
        val action = intent?.action
        when (action) {
            ACTION_DISCONNECT -> {
                disconnect()
            }
        }

        websocketAmbulatory.connect()
        val notification = createNotification()

        bluetoothController.incomingSamples
            .onEach { sample ->
                //Log.w("ECGForegroundService", "ECG sample: $sample")
                // Optionally store to DB or send to server here
                ecgDataProcessingService.parseData(sample, LocalDateTime.now())
            }
            .launchIn(serviceScope)

        bluetoothController.isConnected
            .onEach { isConnected ->
                if (isConnected) {
                    // Update notification if needed
                    val newNotification = createNotification()
                    notificationManager.notify(NOTIFICATION_ID, newNotification)
                    websocketAmbulatory.publishBluetoothIsActive()
                } else {
                    // Connection lost, stop the service
                    stopSelf()
                }
            }
            .launchIn(serviceScope)

        ecgEmergencyBroadcastService.events
            .onEach {
                event ->
                when(event) {
                    is EmergencyEvent.EmergencyReportAmbulatory -> {
                        if (event.reportType == DetectionType.BRADYCARDHIA) {
                            reportRepository.sendTachyOrBradyOrAsys("Tachycardia", LocalDateTime.now())
                        } else if (event.reportType == DetectionType.ASYSTOLE) {
                            reportRepository.sendTachyOrBradyOrAsys("Asystole", LocalDateTime.now())
                        } else if (event.reportType == DetectionType.TACHYCARDHIA) {
                            reportRepository.sendTachyOrBradyOrAsys("Bradycardia", LocalDateTime.now())
                        }
                    }
                    else -> {

                    }
                }

            }.launchIn(serviceScope)

        startForeground(NOTIFICATION_ID, notification)

        return START_NOT_STICKY
    }

    private fun disconnect() {
        Log.i("BluetoothService", "Disconnecting Bluetooth...")
        bluetoothController.closeConnection()
        serviceJob.cancel() // Clean up coroutines
        if (::wakeLock.isInitialized && wakeLock.isHeld) {
            wakeLock.release()
            Log.d("ECGForegroundService", "WakeLock released in onDestroy.")
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()

        websocketAmbulatory.disconnect()
    }

    private fun createNotification(): Notification {
        val disconnectPendingIntent = createDisconnectIntent()
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.heart2heart_logo_black) // Make sure you have this icon
            .setContentTitle("Heart2Heart")
            .setContentText("ECG sedang jalan")
            .addAction(
                R.drawable.close_x,
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
        serviceJob.cancel()
        websocketAmbulatory.disconnect()
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