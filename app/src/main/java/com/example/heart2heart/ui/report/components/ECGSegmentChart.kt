package com.example.heart2heart.ui.report.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.report.domain.model.ECGSegment
import com.example.heart2heart.ui.home.components.chart.ItemPlacerHorizontal
import com.example.heart2heart.ui.home.components.chart.TimeAxisValueFormatter
import com.example.heart2heart.ui.theme.ubuntuFamily
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import com.patrykandpatrick.vico.core.scroll.AutoScrollCondition
import com.patrykandpatrick.vico.core.scroll.InitialScroll
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun ECGSegmentChart(
    ecgSegment: List<ECGSegment> = emptyList<ECGSegment>()
) {

    var ecgPoints by remember { mutableStateOf<ChartEntryModel>(entryModelOf(emptyList<FloatEntry>())) }
    var refTime by remember { mutableStateOf(LocalDateTime.now()) }

    var endTime by remember { mutableStateOf(LocalDateTime.now()) }

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    LaunchedEffect(ecgSegment) {
        var entryCount = -3
        if (ecgSegment.size > 0) {
            val newModel = entryModelOf(
                ecgSegment.map {
                        seg ->
                    entryCount +=3
                    entryOf(entryCount, seg.signal)
                }
            )

            ecgPoints = newModel
            refTime = ecgSegment[0].timestamp
            endTime = refTime.plus(3 * ecgSegment.size.toLong(), ChronoUnit.MILLIS)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Recorded from", color = Color.White, fontFamily = ubuntuFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "${refTime.format(formatter)} - ${endTime.format(formatter)}", color = Color.White, fontFamily = ubuntuFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Spacer(Modifier.height(4.dp).fillMaxWidth())
        Chart(
            chart = lineChart(
                lines = listOf(
                    LineChart.LineSpec(
                        lineColor = Color.White.toArgb(),
                        lineThicknessDp = 2.0f,

                        )
                ),
                spacing = 10.dp,
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
            model = ecgPoints,
            // chartModelProducer = ecgViewModel.chartModelProducer,
            startAxis = rememberStartAxis(
                label = textComponent {
                    color = Color.White.toArgb()
                    textSizeSp = 8f
                },
            ),
            bottomAxis = rememberBottomAxis(
                valueFormatter = TimeAxisValueFormatter(refTime),
//                itemPlacer = AxisItemPlacer.Horizontal.default(
//                    spacing = 5,
//                    offset = 0,
//                ),
                itemPlacer = ItemPlacerHorizontal(
                    spacing = 5,
                    offset = 0,
                ),
                label = textComponent {
                    color = Color.White.toArgb()
                    textSizeSp = 8f
                },
            ),
            getXStep = { it -> 1000.0f},
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)


        )
    }
}