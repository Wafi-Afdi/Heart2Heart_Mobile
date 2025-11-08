package com.example.heart2heart.ui.home.components.deviceConnection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.R
import com.example.heart2heart.ui.theme.poppinsFamily
import com.example.heart2heart.ui.theme.ubuntuFamily
import com.example.heart2heart.utils.CpuChip
import com.example.heart2heart.utils.Ecg_heart
import com.example.heart2heart.utils.Location_on
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDateTime

@Composable
fun DeviceInfoView(
    isConnected: Boolean = true,
    lastConnectionTime: LocalDateTime = LocalDateTime.of(2025, 10, 23, 15, 45, 30),
    onDeviceButtonClick: () -> Unit = {},
    disconnectDevice: () -> Unit = {},
) {

    val now = LocalDateTime.now()
    val duration = Duration.between(lastConnectionTime, now)

    val hours = duration.toHours()
    val minutes = duration.toMinutes() % 60
    val seconds = duration.seconds % 60

    var formattedDuration by remember { mutableStateOf("") }

    val DeviceConnectionInfo = buildAnnotatedString {
        append("Connected Since\n")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(formattedDuration)
        }

    }

    LaunchedEffect(Unit) {
        while (true) {
            val now = LocalDateTime.now()
            val duration = Duration.between(lastConnectionTime, now)
            val hours = duration.toHours()
            val minutes = duration.toMinutes() % 60
            val seconds = duration.seconds % 60
            formattedDuration = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            delay(1000L) // update every 1 second
        }
    }
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 0.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.Start // items-start
        ) {
            Text(
                text = "STM32F4CE",
                style = LocalTextStyle.current.copy(
                    lineHeight = 16.sp // adjust as needed, try 16–20.sp
                ),
                fontFamily = ubuntuFamily,
                color = if (isSystemInDarkTheme()) colorResource(R.color.text_dark) else colorResource(R.color.text_light),
                fontSize = 14.sp,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Ecg_heart,
                        contentDescription = "ECG Icon",
                        tint = if (isSystemInDarkTheme()) colorResource(R.color.text_dark) else colorResource(R.color.text_light),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "181 BPM",
                        style = LocalTextStyle.current.copy(
                            lineHeight = 16.sp
                        ),
                        fontFamily = ubuntuFamily,
                        fontWeight = FontWeight.Bold,
                        color = if (isSystemInDarkTheme()) colorResource(R.color.text_dark) else colorResource(R.color.text_light),
                        fontSize = 14.sp,
                    )
                }
            }
        }
        VerticalDivider(color = colorResource(
            R.color.accent_dark),
            thickness = 2.dp,
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 8.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 8.dp
                    )
                )
                .background(
                    if (isConnected) {
                        colorResource(R.color.success)
                    } else {
                        if (isSystemInDarkTheme()) colorResource(R.color.neutral_900) else colorResource(R.color.neutral_300)
                    }
                )
                .fillMaxHeight()
                .padding(8.dp)
                .clickable {
                    if (!isConnected) {
                        onDeviceButtonClick()
                    }
                    else {
                        disconnectDevice()
                    }
                }
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,

        ) {
            Icon(
                imageVector = CpuChip,
                contentDescription = "Device Icon",
                tint =
                    if (isConnected) {
                        colorResource(R.color.text_dark)
                    } else {
                        if (isSystemInDarkTheme()) colorResource(R.color.text_dark) else colorResource(R.color.neutral_900)
                    }
                ,
                modifier = Modifier.size(36.dp)
            )
            Spacer(Modifier.width(2.dp))
            if (isConnected) {
                Text(
                    text = DeviceConnectionInfo,
                    style = LocalTextStyle.current.copy(
                        lineHeight = 16.sp // adjust as needed, try 16–20.sp
                    ),
                    fontFamily = ubuntuFamily,
                    color = colorResource(R.color.text_dark),
                    fontSize = 14.sp,
                )
            } else {
                Text(
                    text = "Not connected to ECG",
                    style = LocalTextStyle.current.copy(
                        lineHeight = 16.sp // adjust as needed, try 16–20.sp
                    ),
                    fontFamily = ubuntuFamily,
                    color = if (isSystemInDarkTheme()) colorResource(R.color.text_dark) else colorResource(R.color.neutral_900),
                    fontSize = 14.sp,
                )
            }
        }
    }
}