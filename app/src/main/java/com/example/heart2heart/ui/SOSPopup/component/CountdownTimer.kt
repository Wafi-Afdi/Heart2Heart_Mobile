package com.example.heart2heart.ui.SOSPopup.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.R
import com.example.heart2heart.ui.theme.ubuntuFamily
import java.util.concurrent.TimeUnit

@Composable
fun TimerGauge(
    timeRemaining: Long,
    totalDurationMillis: Long,
) {

    val mainColor = colorResource(R.color.warning)
    val tertiary = MaterialTheme.colorScheme.tertiary

    val progress by animateFloatAsState(
        targetValue = timeRemaining / totalDurationMillis.toFloat(),
        animationSpec = androidx.compose.animation.core.tween(
            durationMillis = 200,
            easing = LinearEasing
        ),
        label = "countdownProgress"
    )

    val seconds =
        TimeUnit.MILLISECONDS.toSeconds(timeRemaining)
    val formattedTime = String.format("%d", seconds)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .size(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 8.dp.toPx()
            val diameter = size.minDimension
            val sweepAngle = 360 * progress

            // background circle
            drawArc(
                color = tertiary,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(
                    (size.width - diameter) / 2,
                    (size.height - diameter) / 2
                ),
                size = Size(diameter, diameter),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Foreground (draining) arc
            drawArc(
                color = mainColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(
                    (size.width - diameter) / 2,
                    (size.height - diameter) / 2
                ),
                size = Size(diameter, diameter),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Text(
            text = formattedTime,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = ubuntuFamily,
            color = mainColor
        )
    }
}