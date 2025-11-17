package com.example.heart2heart.ui.statistic.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.R
import com.example.heart2heart.report.domain.model.AverageBPM
import com.example.heart2heart.report.presentation.state.rangeDate
import com.example.heart2heart.ui.theme.poppinsFamily
import com.example.heart2heart.ui.theme.ubuntuFamily
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun StatisticComponent(
    onUpdateDate: (start: LocalDateTime, end: LocalDateTime) -> Unit = {
        st, en ->
        Unit
    },
    listBPMData: List<AverageBPM> = emptyList(),
    rangeDate: rangeDate = rangeDate(
        start = LocalDateTime.now().minusDays(6),
        end = LocalDateTime.now()
    )

) {
    var showDatePicker by remember { mutableStateOf(false) }

    val dateRangePickerState = rememberDateRangePickerState()

    var selectedDateRangeText by remember { mutableStateOf<String?>(null) }

    val format = DateTimeFormatter.ofPattern("MMM dd")

    LaunchedEffect(rangeDate) {
        selectedDateRangeText = "From: ${rangeDate.start.format(format)} -  ${rangeDate.end.format(format)}"
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.secondary)
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .clickable {
                showDatePicker = true
            },
        ) {
            Text(
                text = selectedDateRangeText ?: "Choose a range",
                fontFamily = ubuntuFamily,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp,
                lineHeight = 2.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(Modifier.height(8.dp))

        if(rangeDate.isLoading) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp).align(Alignment.Center),
                    strokeWidth = 2.dp
                )
            }
        } else if(listBPMData.size == 0) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)

            ) {
                Text(
                    text = "No Data",
                    fontFamily = ubuntuFamily,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp,
                    lineHeight = 2.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {

            BPMBarChart(
                listBPMData
            )
        }



        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = {
                    showDatePicker = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDatePicker = false
                            // Get the selected start and end dates
                            val startDateMillis = dateRangePickerState.selectedStartDateMillis
                            val endDateMillis = dateRangePickerState.selectedEndDateMillis


                            if (startDateMillis != null && endDateMillis != null) {
                                onUpdateDate(fromLongToLocalDateTime(startDateMillis), fromLongToLocalDateTime(endDateMillis))
                            } else {
                            }
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DateRangePicker(state = dateRangePickerState)
            }
        }
    }
}

private fun Long.toFormattedDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("MMM dd", Locale.getDefault())
    return format.format(date)
}

private fun LocalDateTime.toMillis(): Long? {
    return this.atZone(ZoneOffset.systemDefault())?.toInstant()?.toEpochMilli()
}

private fun fromLongToLocalDateTime(input: Long): LocalDateTime {
    val instant = Instant.ofEpochMilli(input)

// 2. Get the system's default time zone
    val zoneId = ZoneId.systemDefault()

// 3. Convert the Instant to LocalDateTime using that time zone
    return LocalDateTime.ofInstant(instant, zoneId)
}
