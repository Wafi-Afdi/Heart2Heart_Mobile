package com.example.heart2heart.ui.home.components.chart

import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.values.ChartValues
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.Instant
import java.time.temporal.ChronoUnit

class TimeAxisValueFormatter(
    private val refTime: LocalDateTime
) : AxisValueFormatter<AxisPosition.Horizontal.Bottom> {
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    override fun formatValue(
        value: Float,
        chartValues: ChartValues
    ): CharSequence {
        val epochMillis = value.toLong()
//        val instant = Instant.ofEpochMilli(epochMillis)
//
//        val dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
        val actualDateTime = refTime.plus(epochMillis, ChronoUnit.MILLIS)
        return actualDateTime.format(timeFormatter)
    }

}