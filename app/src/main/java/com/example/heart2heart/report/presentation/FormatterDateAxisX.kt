package com.example.heart2heart.report.presentation

import android.annotation.SuppressLint
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.values.ChartValues
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class FormatterDateAxisX(
    private val refTime: LocalDateTime
) : AxisValueFormatter<AxisPosition.Horizontal.Bottom> {
    @SuppressLint("WeekBasedYear")
    private val timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

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