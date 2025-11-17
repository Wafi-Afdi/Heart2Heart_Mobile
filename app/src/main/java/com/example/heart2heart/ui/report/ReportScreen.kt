package com.example.heart2heart.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.heart2heart.ECGExtraction.data.ReportType
import com.example.heart2heart.EmergencyBroadcast.presentation.state.ReportUIState
import com.example.heart2heart.R
import com.example.heart2heart.ui.report.components.AFibDescription
import com.example.heart2heart.ui.report.components.AsystoleDescription
import com.example.heart2heart.ui.report.components.BradycardiaDescription
import com.example.heart2heart.ui.report.components.ECGSegmentChart
import com.example.heart2heart.ui.report.components.IsProcessingDescription
import com.example.heart2heart.ui.report.components.NormalRhytmDescription
import com.example.heart2heart.ui.report.components.ReportDetailComponent
import com.example.heart2heart.ui.report.components.ReportHeader
import com.example.heart2heart.ui.report.components.TachycardiaDescription
import com.example.heart2heart.ui.report.components.VFibDescription
import com.example.heart2heart.ui.report.components.VentricularTachyDescription
import com.example.heart2heart.utils.Arrow_back
import com.example.heart2heart.utils.PreviewWrapperWithScaffold


@Composable
fun ReportScreen(
    onBackPressed: () -> Unit = {},
    modifier: Modifier,
    reportModel: ReportUIState = ReportUIState(),
) {
    val scrollState = rememberScrollState()

    Box(modifier = modifier.fillMaxSize().padding(16.dp)) {
        IconButton(
            onClick = onBackPressed,
            modifier = Modifier.align(Alignment.TopStart).size(30.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.back_svg),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "Back",
                modifier = Modifier.requiredSize(24.dp)
            )
        }
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = 27.dp)
        ) {
            ReportHeader(
                reportType = reportModel.reportType,
                timestamp = reportModel.timestamp,
                name = reportModel.username,
            )
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp,
                color = if (isSystemInDarkTheme()) colorResource(R.color.neutral_900) else colorResource(R.color.neutral_700)
            )
            Spacer(Modifier.height(8.dp))
            ECGSegmentChart()
            Spacer(Modifier.height(8.dp))
            ReportDetailComponent(
                title = setTitle(reportModel.reportType),
                description = setDescription(reportModel.reportType)
            )
        }
    }

}

private fun setTitle(reportType: ReportType): String {
    return when(reportType) {
        ReportType.TACHYCARDHIA -> "Tachycardia"
        ReportType.BRADYCARDHIA -> "Bradycardia"
        ReportType.ASYSTOLE -> "Asystole"
        ReportType.VFIB -> "Ventricular Fibrillation"
        ReportType.AFIB -> "Atrial Fibrillation"
        ReportType.VT -> "Ventricular Tachycardia"
        ReportType.UNKNOWN -> "Is Being Processed"
        ReportType.NORMAL -> "Normal Rhythm"
        ReportType.SOS -> "SOS"
    }
}
private fun setDescription(reportType: ReportType): String {
    return when(reportType) {
        ReportType.TACHYCARDHIA -> TachycardiaDescription
        ReportType.BRADYCARDHIA -> BradycardiaDescription
        ReportType.ASYSTOLE -> AsystoleDescription
        ReportType.VFIB -> VFibDescription
        ReportType.AFIB -> AFibDescription
        ReportType.VT -> VentricularTachyDescription
        ReportType.UNKNOWN -> IsProcessingDescription
        ReportType.NORMAL -> NormalRhytmDescription
        ReportType.SOS -> "SOS signal is passed manually by the ambulatory user when they press the SOS button"
    }
}


@Preview(showBackground = true, name = "My Component in a Scaffold")
@Composable
private fun Preview() {
    PreviewWrapperWithScaffold {
            innerPadding ->
        ReportScreen(modifier = Modifier.padding(innerPadding))
    }
}

