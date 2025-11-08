package com.example.heart2heart.ECGExtraction.presentation

import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heart2heart.ECGExtraction.domain.ECGForegroundService
import com.example.heart2heart.bluetooth.BluetoothServiceECG
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.compose
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

@HiltViewModel
class ECGChartViewModel @Inject constructor(
    private val bluetoothServiceEcg: BluetoothServiceECG
) : ViewModel() {
    data class Point(val x: Float, val y: Float)

    private val _points = MutableStateFlow<List<Point>>(emptyList())
    val points = _points.asStateFlow()

    private val _chartModelProducer = ChartEntryModelProducer()
    val chartModelProducer: ChartEntryModelProducer
        get() = _chartModelProducer
    private val dataPoints = listOf(4f, 12f, 8f, 16f, 11f, 14f, 10f,20f,3f,4f,5f,6f,5f,1f,2f)


    private val SAMPLE_INTERVAL_MS = 40L         // 25 Hz sampling
    private val MAX_POINTS = 500                 // sliding window size
    private val BASE_AMPLITUDE = 6f
    private val NOISE_LEVEL = 0.4f

    // Internal buffer and state
    private val buffer = ArrayDeque<Point>(MAX_POINTS)
    private var xCounter = 0f
    private var phase = 0.0

    init {
        // startGenerating()
        subscribeECGDataFromBluetooth()
    }

    private fun addPoint(point: Point) {
        if (buffer.size >= MAX_POINTS) buffer.removeFirst()
        buffer.addLast(point)
        _points.value = buffer.toList()
//        _chartModelProducer.setEntries(
//            if (_points.value.isEmpty()) {
//                    dataPoints.mapIndexed {
//                            value, index ->
//                        entryOf(value, index)
//                    }
//            } else {
//                val baseX = _points.value.first().x
//                _points.value.map { p ->
//                    // subtract baseX so the x range is from 0 .. windowWidth
//                    entryOf((p.x - baseX), p.y)
//                }
//            }
//        )
    }

    private fun startGenerating() {
        viewModelScope.launch {
            while (false) {
                val y = generateEcgLikeSample()
                addPoint(Point(xCounter, y))
                xCounter += 0.04f
                delay(SAMPLE_INTERVAL_MS)
            }
        }
    }

    private fun subscribeECGDataFromBluetooth() {
        viewModelScope.launch {
            bluetoothServiceEcg
                .incomingSamples
                .collect {
                    data ->
                    addPoint(Point(xCounter, data.toFloat()))
                    xCounter += 0.04f
                }
        }
    }


    private fun generateEcgLikeSample(): Float {
        // base sinusoid (low-frequency)
        val freq = 1.2 // cycles per second (adjust for look)
        // increment phase according to sample rate
        val dt = SAMPLE_INTERVAL_MS / 1000.0
        phase += 2.0 * PI * freq * dt
        // clean sinusoidal baseline
        val base = (sin(phase) * BASE_AMPLITUDE).toFloat()

        // occasional sharp spike to look like a QRS complex
        val spikeProbabilityPerSample = 0.03f
        val spike = if (Random.nextFloat() < spikeProbabilityPerSample) {
            // create a short positive spike
            BASE_AMPLITUDE * 3f
        } else {
            0f
        }

        // small random noise
        val noise = (Random.nextFloat() - 0.5f) * NOISE_LEVEL

        return base
    }

    // public controls (optional)
    fun clear() {
        buffer.clear()
        _points.value = emptyList()
        xCounter = 0f
    }

}