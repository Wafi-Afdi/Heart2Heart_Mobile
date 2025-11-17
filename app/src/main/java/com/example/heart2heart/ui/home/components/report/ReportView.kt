package com.example.heart2heart.ui.home.components.report

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.ECGExtraction.data.ReportType
import com.example.heart2heart.R
import com.example.heart2heart.report.domain.model.ReportData
import com.example.heart2heart.ui.theme.poppinsFamily
import com.example.heart2heart.ui.theme.ubuntuFamily
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ReportView(
    onClickSeeMoreTop: () -> Unit = {},
    onClickSeeMoreCard: (id: String) -> Unit = {},
    listOfReport: List<ReportData> = emptyList<ReportData>(),
    showIsSeeMore: Boolean = true
) {
    val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(
        "dd/MM/yyyy HH:mm"
    )
        // It's generally a good practice to specify a locale
        .withLocale(Locale.getDefault())

    Column(
        modifier = Modifier
            .padding(horizontal = if (showIsSeeMore) { 16.dp} else { 0.dp}, vertical = 0.dp)
            .fillMaxWidth()
        ,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Report",
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
            )

            if(showIsSeeMore) {
                Text(
                    "See more",
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable{
                        onClickSeeMoreTop()
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (listOfReport.isEmpty()) {
                Text(
                    "No Reports",
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                listOfReport.map {
                        it ->
                    val descriptiveText = buildAnnotatedString {
                        when (it.detectionType) {
                            ReportType.SOS -> {
                                append(it.username + " ")
                                append("sent a SOS signal")
                            }
                            ReportType.UNKNOWN -> {
                                append(it.username + "'s ")
                                append("report is still being processed")
                            }
                            else -> append(it.username + " was reported with " +  reportTypeToStringTitle(it.detectionType))
                        }
                        append()
                    }
                    ReportCard(
                        onClickSeeMore = {
                            onClickSeeMoreCard(it.reportId.toString())
                        },
                        title = reportTypeToStringTitle(it.detectionType),
                        isOk = it.detectionType == ReportType.NORMAL,
                        timestamp = it.ts.format(DATE_TIME_FORMATTER),
                        description = descriptiveText.toString()
                    )
                }
            }
        }
        /*Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
            ,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonColors(
                containerColor = colorResource(R.color.primary_light),
                contentColor = colorResource(R.color.text_dark),
                disabledContentColor = colorResource(R.color.neutral_900),
                disabledContainerColor = colorResource(R.color.neutral_700)
            )
        ) {
            // Button content
            Text(
                text = "Load More",
                modifier = Modifier.padding(vertical = 4.dp),
                fontFamily = ubuntuFamily,
                fontSize = 16.sp,
            )
        }*/
    }
}

private fun reportTypeToStringTitle(reportType: ReportType): String {
    return when (reportType) {
        ReportType.SOS -> "SOS"
        ReportType.AFIB -> "Atrial Fibrillation"
        ReportType.VT -> "Ventricular Tachycardia"
        ReportType.VFIB -> "Ventricular Fibrillation"
        ReportType.BRADYCARDHIA -> "Bradycardia"
        ReportType.TACHYCARDHIA -> "Tachycardia"
        ReportType.ASYSTOLE -> "Asystole"
        ReportType.NORMAL -> "Normal Rhythm"
        ReportType.UNKNOWN -> "Is Being Processed"
    }
}