package com.example.heart2heart.ui.statistic.component

import android.graphics.ColorSpace
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toLong
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.heart2heart.report.domain.model.AverageBPM
import com.example.heart2heart.report.presentation.FormatterDateAxisX
import com.example.heart2heart.ui.home.components.chart.TimeAxisValueFormatter
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.core.chart.column.ColumnChart
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.ChartModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import com.patrykandpatrick.vico.core.scroll.AutoScrollCondition
import com.patrykandpatrick.vico.core.scroll.InitialScroll
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun BPMBarChart(
    listBPMData: List<AverageBPM> = emptyList()
) {
    val zoneId = ZoneId.of("Asia/Jakarta")
    val modelProducer = remember { ChartEntryModelProducer() }
    var refTime: LocalDateTime? by remember { mutableStateOf(null) }
    LaunchedEffect(listBPMData) {
        if (listBPMData.size > 0) {

            val series1 = listOf(
                listBPMData.map {
                    it
                    entryOf(
                        Duration.between(listBPMData[0].timestamp, it.timestamp).toMillis().toFloat(),
                        y = it.bpm
                    )
                }
            )
            // Load the data into the producer
            modelProducer.setEntries(series1)
            refTime = listBPMData[0].timestamp
        } else {
            refTime = null
        }
    }

    Chart(
        chart = columnChart(
            columns = listOf(
                LineComponent(
                    color = Color.White.toArgb(),
                    ),
            ),
            spacing = 10.dp,
            innerSpacing = 0.dp,
        ),
        chartScrollSpec = rememberChartScrollSpec(
            initialScroll = InitialScroll.End,
            autoScrollCondition = AutoScrollCondition.OnModelSizeIncreased,
            isScrollEnabled = true,
            autoScrollAnimationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
        ),
        startAxis = rememberStartAxis(
            label = textComponent {
                color = Color.White.toArgb()
                textSizeSp = 8f
            },
        ),
        bottomAxis = rememberBottomAxis(
            valueFormatter = FormatterDateAxisX(refTime ?: LocalDateTime.now()),
            label = textComponent {
                color = Color.White.toArgb()
                textSizeSp = 8f
            },
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        chartModelProducer = modelProducer

    )
}