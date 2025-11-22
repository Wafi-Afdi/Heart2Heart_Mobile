package com.example.heart2heart.ui.report

import android.text.Layout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.heart2heart.EmergencyBroadcast.presentation.ReportViewModel
import java.util.UUID

@Composable
fun ReportScreenRoute(
    reportId: String,
    onBackPressed: () -> Unit = {},
    modifier: Modifier,
    reportViewModel: ReportViewModel
) {

    val reportModel by reportViewModel.reportModel.collectAsState()

    val isLoading by reportViewModel.isLoadingReport.collectAsState()

    LaunchedEffect(Unit) {
        reportViewModel.fetchReport(UUID.fromString(reportId))
    }

    if (isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        ReportScreen(
            modifier = modifier,
            onBackPressed = onBackPressed,
            reportModel = reportModel,
            onDownloadData = {
                reportViewModel.onDownloadPressed()
            }
        )
    }

}