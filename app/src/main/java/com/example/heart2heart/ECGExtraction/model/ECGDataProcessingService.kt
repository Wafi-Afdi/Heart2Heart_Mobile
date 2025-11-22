package com.example.heart2heart.ECGExtraction.model

import android.util.Log
import com.example.heart2heart.ECGExtraction.data.DetectionType
import com.example.heart2heart.ECGExtraction.data.dto.BpmDataDTO
import com.example.heart2heart.ECGExtraction.data.dto.BpmPublishDTO
import com.example.heart2heart.ECGExtraction.data.dto.EcgDataDTO
import com.example.heart2heart.ECGExtraction.data.dto.publishEcgDTO
import com.example.heart2heart.ECGExtraction.data.repository.BpmRepositoryImpl
import com.example.heart2heart.ECGExtraction.repository.ECGRepository
import com.example.heart2heart.EmergencyBroadcast.domain.EmergencyBroadcastService
import com.example.heart2heart.auth.repository.ProfileRepository
import com.example.heart2heart.bluetooth.BluetoothServiceECG
import com.example.heart2heart.websocket.data.dto.LiveDataDTO
import com.example.heart2heart.websocket.data.dto.SignalDTO
import com.example.heart2heart.websocket.repository.WebSocketRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.random.Random

private data class ecgRaw (
    val signal: Float,
    val rP: Boolean,
    val asystole: Boolean
)
class ECGDataProcessingService @Inject constructor(
    private val ecgRepository: ECGRepository,
    private val bpmRepositoryImpl: BpmRepositoryImpl,
    private val profileRepository: ProfileRepository,
    private val emergencyBroadcastService: EmergencyBroadcastService,
    private val bluetoothServiceECG: BluetoothServiceECG,
    private val webSocketRepository: WebSocketRepository
) {
    private val processingScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val buffer: ArrayDeque<ECGSignalDataSTM> = ArrayDeque()
    private val bufferMutex = Mutex()

    private val _calculatedBPM = MutableStateFlow<Int>(0)
    val calculatedBPM: StateFlow<Int>
        get() = _calculatedBPM.asStateFlow()

    private var totalBeats = 0U
    private val bufferBeats = Mutex()

    private var isAsystoleDetected = false;
    private var isTachyDetected = false;
    private var isBradyDetected = false;

    private var lastDetectedBrady: Long? = null
    private var lastDetectedAsytole: Long? = null
    private var spamAsystole: Int = 0;

    private var lastDetectedTachy: Long? = null

    private var lastRecordBPM = LocalDateTime.now()
    private var accumulatedTime = 0L
    private var totalBPMCount = 0;
    private var totalBPMAcc = 0L;

    private var totalTachyCount = 0;
    private var totalBradyCount = 0;

    private var counterInterval = 0L

    private var latestReceiveLocalTime = LocalDateTime.now()

    private val _saveSignal = MutableSharedFlow<Unit>(
        replay = 0,
        extraBufferCapacity = 0
    )
    val saveSignal: SharedFlow<Unit> = _saveSignal

    init {
        Log.i("EcgProcessingService", "init")
        startBufferProcessor()
    }

    suspend fun parseData(rawData: String, timestamp: LocalDateTime) {

        if (ChronoUnit.MILLIS.between(timestamp, latestReceiveLocalTime) > 10) {
            counterInterval = 0
            latestReceiveLocalTime = timestamp
        }
        val newECG = parseECG(rawData)
        val newData = ECGSignalDataSTM(
            signal = newECG.signal,
            RRPeak = newECG.rP,
            asystole = newECG.asystole,
            intervalTime = counterInterval,
            recordTime = latestReceiveLocalTime.plus(counterInterval, ChronoUnit.MILLIS)
        )
        counterInterval += 3
        if (newECG.rP) {
            totalBeats++
        }
        if (newECG.asystole && !isAsystoleDetected) {
            if (lastDetectedAsytole != null && System.currentTimeMillis() - lastDetectedAsytole!! >= 300_000 && spamAsystole <= 3) {
                 emergencyBroadcastService.emitReportAmbulatory(reportType = DetectionType.ASYSTOLE)
                 isAsystoleDetected = true
                 lastDetectedAsytole = System.currentTimeMillis()
                 spamAsystole++
            }

            // Only run once
            if (lastDetectedAsytole == null) {
                 emergencyBroadcastService.emitReportAmbulatory(reportType = DetectionType.ASYSTOLE)
                 lastDetectedAsytole = System.currentTimeMillis();
                 isAsystoleDetected = true;
            }
        } else if (!newECG.asystole && isAsystoleDetected) {
            isAsystoleDetected = false
            spamAsystole = 0
        }
        bufferMutex.withLock {
            buffer.add(newData)
            if (buffer.size >= 300) {
                _saveSignal.emit(Unit)
            }
        }
    }

    private fun parseECG(raw: String): ecgRaw {
        val parts = raw.split(',')
        try {
            if (parts.size == 3) {
                val value1 = parts[0].toFloat()
                val value2 = parts[1].toInt() == 1
                val value3 = parts[2].toInt() == 1
                return ecgRaw(value1, value2, value3)
            } else {
                return ecgRaw(0f, false, false)
            }
        } catch(e: Exception) {
            Log.e("EXCEP", "${parts.size}")
            return ecgRaw(0f, false, false)
        }
    }

    private fun startBufferProcessor() {
        processingScope.launch {
            _saveSignal.collect {
                handleBufferSave()
            }
        }
        processingScope.launch {
            while (true) {
                checkBPM()
                delay(2_500L)
            }
        }

        processingScope.launch {
            webSocketRepository.liveDataFlow.collect {
                data ->
                _calculatedBPM.update { data.bpm }
            }
        }
    }

    private suspend fun handleBufferSave() {
        var dataToSave: List<ECGSignalDataSTM> = emptyList()
        bufferMutex.withLock {
            if (!buffer.isEmpty() && buffer.size >= 300) {
                // Get a copy and clear the buffer
                dataToSave = buffer.toList()
                buffer.clear()
            }
        }

        var liveDataDTO = LiveDataDTO(
            ecgList = dataToSave.map {
                it ->
                it.signal
            },
            start = dataToSave[0].recordTime.toString(),
            bpm = _calculatedBPM.value
        )
        webSocketRepository.publish("/app/liveData", Json.encodeToString(liveDataDTO))

        if (dataToSave.isNotEmpty()) {
            // ecgRepository.saveBatch(dataToSave)
            ecgRepository.saveBatchToDb(dataDTO = publishEcgDTO(
                userId = UUID.fromString(profileRepository.userData.value.id),
                ecgData = dataToSave.map {
                    it ->
                    EcgDataDTO(
                        signal = it.signal,
                        rp = it.RRPeak ?: false,
                        flat = it.asystole ?: false,
                        ts = it.recordTime.toString(),
                    )
                }
            ))
        }
    }

    private suspend fun checkBPM() {
        if (bluetoothServiceECG.isConnected.value) {
            val now = LocalDateTime.now()
            val deltaTime = Duration.between(lastRecordBPM, now)
            accumulatedTime += deltaTime.toMillis()
            if (deltaTime.toMillis() >= 3_000L) {
                val beatsPerSecond = totalBeats.toFloat() / (deltaTime.toMillis() / 1000.0f)
                val beatsPerMinute = beatsPerSecond * 60.0f
                _calculatedBPM.update { beatsPerMinute.toInt() }
                bufferBeats.withLock {
                    totalBPMAcc += totalBeats.toLong()
                    totalBeats = 0U
                }
                totalBPMCount++;
                lastRecordBPM = now
            }

            if (accumulatedTime >= 30_000) {
                val average = totalBPMAcc.toFloat() / totalBPMCount.toFloat()
                totalBPMAcc = 0
                totalBPMCount = 0
                var dataDTO = BpmDataDTO(
                    bpm = average,
                    ts = LocalDateTime.now().toString(),
                )
                val datasDTO: List<BpmDataDTO> = listOf(dataDTO)
                if (average > 100) {
                    totalTachyCount++
                } else {
                    if(isTachyDetected) {
                        isTachyDetected = false
                    }
                    totalTachyCount = 0
                }

                if (average < 60) {
                    totalBradyCount++
                } else {
                    if(isBradyDetected) {
                        isBradyDetected = false
                    }
                    totalBradyCount = 0
                }

                if (totalTachyCount > 3 && !isTachyDetected) {
                    emergencyBroadcastService.emitReportAmbulatory(DetectionType.TACHYCARDHIA)
                    totalTachyCount = 0
                    isTachyDetected = true
                }

                if (totalBradyCount > 3  && !isBradyDetected) {
                    emergencyBroadcastService.emitReportAmbulatory(DetectionType.BRADYCARDHIA)
                    totalBradyCount = 0
                }  else if (isBradyDetected) {
                    totalBradyCount = 0
                }

                bpmRepositoryImpl.sendBPMData(datasDTO, userId = UUID.fromString(profileRepository.userData.value.id))
        }
        }
    }

    fun close() {
        processingScope.cancel()
    }
}
