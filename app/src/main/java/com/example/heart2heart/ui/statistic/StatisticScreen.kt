package com.example.heart2heart.ui.statistic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.contacts.data.ContactUIState
import com.example.heart2heart.report.domain.model.AverageBPM
import com.example.heart2heart.report.domain.model.ReportData
import com.example.heart2heart.report.presentation.state.rangeDate
import com.example.heart2heart.ui.contact.ContactScreen
import com.example.heart2heart.ui.home.components.report.ReportView
import com.example.heart2heart.ui.statistic.component.ExportComponent
import com.example.heart2heart.ui.statistic.component.GenerateReportAction
import com.example.heart2heart.ui.statistic.component.StatisticComponent
import com.example.heart2heart.ui.theme.poppinsFamily
import com.example.heart2heart.utils.PreviewWrapperWithScaffold
import java.time.LocalDateTime

@Composable
fun StatisticScreen(
    modifier: Modifier,
    onClickExport: () -> Unit = { },
    onClickDiagnose: () -> Unit = { },
    onUpdateBPMRange:(start: LocalDateTime, end: LocalDateTime) -> Unit = {
        st, en ->
        Unit
    },
    listBPMData: List<AverageBPM> = emptyList(),
    listOfReports: List<ReportData> = emptyList(),
    onClickSeeMoreCard: (id: String) -> Unit = {},
    rangeDate: rangeDate = rangeDate(
        start = LocalDateTime.now().minusDays(6),
        end = LocalDateTime.now()
    ),
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = { },
) {
    val scrollState = rememberScrollState()

    PullToRefreshBox(
        modifier = modifier.fillMaxSize().padding(16.dp),
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Text(
                "Statistics",
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
            )
            Spacer(Modifier.height(8.dp))

            StatisticComponent(
                onUpdateBPMRange,
                listBPMData,
                rangeDate = rangeDate,
            )

            Spacer(Modifier.height(8.dp))

            GenerateReportAction(onClick = onClickDiagnose)

            Spacer(Modifier.height(8.dp))

            ExportComponent(onClick = onClickExport)

            Spacer(Modifier.height(16.dp))

            ReportView(
                showIsSeeMore = false,
                onClickSeeMoreCard = onClickSeeMoreCard,
                listOfReport = listOfReports,
            )
        }
    }
}

@Preview(showBackground = true, name = "My Component in a Scaffold")
@Composable
fun PreviewContactScreen() {

    PreviewWrapperWithScaffold {
            innerPadding ->
        StatisticScreen(modifier = Modifier.padding(innerPadding))
    }
}