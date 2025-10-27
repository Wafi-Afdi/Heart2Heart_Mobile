package com.example.heart2heart.ui.home.components.chart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun LiveDataChart(
    dataPoints: List<Float>,
    modifier: Modifier = Modifier
) {
    val chartEntryModel = entryModelOf(dataPoints.mapIndexed { index, value ->
        entryOf(index, value)
    })

    Chart(
        chart = lineChart(),
        model = chartEntryModel,
        modifier = modifier
    )
}

@Composable
fun LiveDataScreen() {
    var dataPoints by remember { mutableStateOf(listOf<Float>()) }

    // Simulate live data updates
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000) // Update every second
            dataPoints = dataPoints + Random.nextFloat() * 100
            if (dataPoints.size > 20) {
                dataPoints = dataPoints.drop(1) // Keep last 20 points
            }
        }
    }

    LiveDataChart(
        dataPoints = dataPoints,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}