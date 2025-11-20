package com.example.heart2heart.ui.statistic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.R
import com.example.heart2heart.ui.theme.poppinsFamily
import com.example.heart2heart.ui.theme.ubuntuFamily
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


private enum class PickerStep {
    DATE_RANGE_SELECTION,
    START_TIME_SELECTION,
    END_TIME_SELECTION,
    NONE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportBottomSheet(
    onSubmit: (start: LocalDateTime, end: LocalDateTime) -> Unit = {st, end -> },
    isLoading: Boolean = false,
) {
    val isDark = isSystemInDarkTheme()

    var currentStep by remember { mutableStateOf(PickerStep.NONE) }

    var showDatePicker by remember { mutableStateOf(false) }

    val dateRangePickerState = rememberDateRangePickerState()
    val dateRangeState = rememberDateRangePickerState()
    val startTimeState = rememberTimePickerState(is24Hour = true)
    val endTimeState = rememberTimePickerState(is24Hour = true)

    var selectedStartMillis by remember { mutableStateOf<Long?>(null) }
    var selectedEndMillis by remember { mutableStateOf<Long?>(null) }

    var selectedStartDateTime by remember { mutableStateOf<LocalDateTime?>(null) }
    var selectedEndDateTime by remember { mutableStateOf<LocalDateTime?>(null) }

    var selectedDateRangeText by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
        Text(
            "Ekspor Data ECG",
            fontFamily = ubuntuFamily,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
        )
        Text(
            "Data ECG berupa CSV dan tentukan rentang waktunya",
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Normal,
            color = if (isDark) colorResource(R.color.neutral_300)  else colorResource(R.color.neutral_900),
            fontSize = 14.sp,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Pilih tanggal",
            fontFamily = ubuntuFamily,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.secondary)
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .clickable {
                    currentStep = PickerStep.DATE_RANGE_SELECTION
                },
        ) {
            Text(
                text = selectedDateRangeText ?: "Choose a range",
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 14.sp,
                lineHeight = 2.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                if (selectedStartDateTime != null && selectedEndDateTime != null) {
                    onSubmit(selectedStartDateTime!!, selectedEndDateTime!!)
                }
                      },
            modifier = Modifier
                .fillMaxWidth()
            ,
            enabled = !isLoading,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor = colorResource(R.color.neutral_900),
                disabledContainerColor = colorResource(R.color.neutral_700)
            )
        ) {
            // Button content
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Download",
                    modifier = Modifier.padding(vertical = 4.dp),
                    fontFamily = ubuntuFamily,
                    fontSize = 16.sp,
                )
            }
        }

        if (currentStep == PickerStep.DATE_RANGE_SELECTION) {
            DatePickerDialog(
                onDismissRequest = { currentStep = PickerStep.NONE },
                confirmButton = {
                    TextButton(
                        onClick = {
                            selectedStartMillis = dateRangeState.selectedStartDateMillis
                            selectedEndMillis = dateRangeState.selectedEndDateMillis

                            // Move to the next step: selecting the START time
                            currentStep = PickerStep.START_TIME_SELECTION
                        },
                        // Only enable 'NEXT' if both dates are selected
                        enabled = dateRangeState.selectedStartDateMillis != null &&
                                dateRangeState.selectedEndDateMillis != null
                    ) {
                        Text("NEXT: Start Time")
                    }
                }
            ) {
                DateRangePicker(state = dateRangeState)
            }
        }

        if (currentStep == PickerStep.START_TIME_SELECTION) {
            TimePickerDialog(
                onDismissRequest = { currentStep = PickerStep.NONE },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Start time selected. Move to the next step: selecting the END time
                            currentStep = PickerStep.END_TIME_SELECTION
                        }
                    ) {
                        Text("NEXT: End Time")
                    }
                },
                title = {  Text("Select Start Time") },
            ) {
                TimePicker(state = startTimeState)
            }
        }

        if (currentStep == PickerStep.END_TIME_SELECTION) {
            TimePickerDialog(
                onDismissRequest = { currentStep = PickerStep.NONE },
                title = { Text("Select End Time") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // All components selected. Combine and finish.
                            currentStep = PickerStep.NONE

                            selectedStartDateTime = toLocalDateTime(
                                dateRangeState.selectedStartDateMillis,
                                startTimeState.hour,
                                startTimeState.minute
                            )

                            selectedEndDateTime = toLocalDateTime(
                                dateRangeState.selectedEndDateMillis,
                                endTimeState.hour,
                                endTimeState.minute
                            )

                            selectedDateRangeText = formatDateTimeRange(
                                dateRangeState.selectedStartDateMillis,
                                dateRangeState.selectedEndDateMillis,
                                startTimeState.hour,
                                startTimeState.minute,
                                endTimeState.hour,
                                endTimeState.minute
                            )
                        }
                    ) {
                        Text("FINISH")
                    }
                }
            ) {
                TimePicker(state = endTimeState)
            }
        }
    }

}

private fun formatDateTimeRange(
    startDateMillis: Long?,
    endDateMillis: Long?,
    startHour: Int,
    startMinute: Int,
    endHour: Int,
    endMinute: Int
): String? {
    if (startDateMillis == null || endDateMillis == null) {
        return null
    }

    val startCalendar = Calendar.getInstance().apply {
        timeInMillis = startDateMillis
        set(Calendar.HOUR_OF_DAY, startHour)
        set(Calendar.MINUTE, startMinute)
        set(Calendar.SECOND, 0)
        timeZone = TimeZone.getDefault()
    }

    val endCalendar = Calendar.getInstance().apply {
        timeInMillis = endDateMillis
        set(Calendar.HOUR_OF_DAY, endHour)
        set(Calendar.MINUTE, endMinute)
        set(Calendar.SECOND, 0)
        timeZone = TimeZone.getDefault()
    }

    val formatter = SimpleDateFormat("MMM dd HH:mm", Locale.getDefault())

    val formattedStart = formatter.format(startCalendar.time)
    val formattedEnd = formatter.format(endCalendar.time)

    return "$formattedStart - $formattedEnd"
}

private fun toLocalDateTime(
    dateMillis: Long?,
    hour: Int,
    minute: Int
): LocalDateTime? {
    if (dateMillis == null) return null

    // 1. Convert Long milliseconds to Instant
    val instant = Instant.ofEpochMilli(dateMillis)

    // 2. Convert Instant to LocalDateTime at the system's default time zone
    val localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()

    // 3. Set the hour and minute from the TimePicker
    return localDateTime
        .withHour(hour)
        .withMinute(minute)
        .withSecond(0)
        .withNano(0)
}