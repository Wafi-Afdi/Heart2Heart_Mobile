package com.example.heart2heart.ui.home.components.chart

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.heart2heart.ECGExtraction.presentation.ECGChartViewModel
import com.example.heart2heart.R
import com.example.heart2heart.ui.theme.ubuntuFamily
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.entry.collectAsState
import com.patrykandpatrick.vico.compose.chart.layout.fullWidth
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import com.patrykandpatrick.vico.core.scroll.AutoScrollCondition
import com.patrykandpatrick.vico.core.scroll.InitialScroll
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

@Composable
fun ChartView(
    isConnected: Boolean = false,
    userBeingViewed: String? = "Nobody"
) {
    val dataPoints = listOf(4f, 12f, 8f, 16f, 11f, 14f, 10f,20f,3f,4f,5f,6f,5f,1f,2f)

    val ecgViewModel = hiltViewModel<ECGChartViewModel>()
    val pointsState by ecgViewModel.points.collectAsState()
    val refTime by ecgViewModel.referenceTime.collectAsState()


    val chartEntryModel = remember(pointsState) {
        if (pointsState.isEmpty()) {
            entryModelOf(
                dataPoints.mapIndexed {
                    value, index ->
                    entryOf(value * 1000, index)
                }
            )
        } else {
            val baseX = pointsState.first().x
            entryModelOf(
                pointsState.map { p ->
                    // subtract baseX so the x range is from 0 .. windowWidth
                    entryOf((p.x), p.y)
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "${userBeingViewed}'s Heart", color = Color.White, fontFamily = ubuntuFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = if (isConnected) "Live" else "Offline", color = Color.White, fontFamily = ubuntuFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
                axisValuesOverrider = AxisValuesOverrider.fixed(minY = -1000f, maxY = 1000f)
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
            model = chartEntryModel,
            // chartModelProducer = ecgViewModel.chartModelProducer,
            startAxis = rememberStartAxis(
                label = textComponent {
                    color = Color.White.toArgb()
                    textSizeSp = 8f
                },
            ),
            bottomAxis = rememberBottomAxis(
                valueFormatter = TimeAxisValueFormatter(refTime ?: LocalDateTime.now()),
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