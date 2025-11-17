package com.example.heart2heart.EmergencyBroadcast.domain

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RawRes
import com.example.heart2heart.ECGExtraction.data.DetectionType
import com.example.heart2heart.EmergencyBroadcast.data.repository.ReportRepository
import com.example.heart2heart.EmergencyBroadcast.domain.data.EmergencyEvent
import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.auth.repository.ProfileRepository
import com.example.heart2heart.home.domain.LiveLocationService
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime
import javax.inject.Inject

class EmergencyBroadcastService @Inject constructor(
    private val context: Context,
    private val profileRepository: ProfileRepository
) {
    private var mediaPlayer: MediaPlayer? = null

    private val _events = MutableSharedFlow<EmergencyEvent>()
    val events: SharedFlow<EmergencyEvent> = _events

    private fun getVibrator(): Vibrator {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    @SuppressLint("MissingPermission")
    fun vibrate() {
        val vibrator = getVibrator()
        // Vibrate for a short duration (e.g., 100ms)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(100)
        }
    }


    suspend fun emitSOSPassed() {
        _events.emit(EmergencyEvent.EmergencySent)
    }

    fun playSound(@RawRes soundResId: Int) {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            return
        }
        val playbackAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        mediaPlayer = MediaPlayer.create(context, soundResId)
        mediaPlayer?.setVolume(1f,1f);

        mediaPlayer?.setAudioAttributes(playbackAttributes)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    fun stopAndReleaseFocus() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    suspend fun emitSOSObserver(username: String) {
        _events.emit(EmergencyEvent.EmergencySOSObserver(username))
    }

    suspend fun emitReportAmbulatory(reportType: DetectionType) {
        _events.emit(EmergencyEvent.EmergencyReportAmbulatory(reportType))
    }

    suspend fun emitReportObserver(username: String, reportType: DetectionType) {
        _events.emit(EmergencyEvent.EmergencyReportObserver(reportType, username))
    }

    suspend fun emitReport(username: String?, reportType: DetectionType) {
        if (profileRepository.appType.value == AppType.AMBULATORY) {
            emitReportAmbulatory(reportType)
        } else if (username != null) {
            emitReportObserver(username, reportType)
        }
    }
}