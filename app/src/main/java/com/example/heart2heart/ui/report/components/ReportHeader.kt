package com.example.heart2heart.ui.report.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.ECGExtraction.data.DetectionType
import com.example.heart2heart.ECGExtraction.data.ReportType
import com.example.heart2heart.ECGExtraction.data.toText
import com.example.heart2heart.R
import com.example.heart2heart.ui.theme.poppinsFamily
import com.example.heart2heart.ui.theme.ubuntuFamily
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ReportHeader(
    reportType: ReportType = ReportType.NORMAL,
    name: String = "Wafi Afdi",
    timestamp: LocalDateTime = LocalDateTime.parse("2025-11-15T19:21:30.123456789")

) {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss")
        .withLocale(Locale.ENGLISH)

    val stringHeader = buildAnnotatedString {
        append("$name ")
        withStyle(style = SpanStyle(

        )) {
            if (reportType == ReportType.SOS) {
                append("activated SOS Emergency")
            }
            else {
                append("reported with ${reportType.toText()}")
            }
        }
    }

    val formattedString = timestamp.format(formatter)

    Column(Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(
            painter = painterResource(R.drawable.panic_heart),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .widthIn(max = 200.dp),
            contentDescription = "Heart Problem Symbol",
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringHeader,
            fontFamily = ubuntuFamily,
            maxLines = 3,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = formattedString,
            fontFamily = poppinsFamily,
            maxLines = 1,
            color = if (isSystemInDarkTheme()) colorResource(R.color.neutral_300) else colorResource(R.color.neutral_900),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            overflow = TextOverflow.Ellipsis,
        )
    }
}