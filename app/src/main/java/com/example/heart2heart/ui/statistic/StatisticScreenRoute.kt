package com.example.heart2heart.ui.statistic

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.heart2heart.report.presentation.StatisticViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticScreenRoute(
    modifier: Modifier,
    statisticViewModel: StatisticViewModel,
    navToDetail: (id: String) -> Unit = { }
) {
    val exportSheetState = rememberModalBottomSheetState()
    var isExportSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }

    val diagnoseSheetState = rememberModalBottomSheetState()
    var isDiagnoseSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }

    val listOfReports by statisticViewModel.listOfReports.collectAsState()

    val listBPMData by statisticViewModel.listOfChartBPM.collectAsState()

    val rangeDate by statisticViewModel.rangeDateBPM.collectAsState()
    StatisticScreen(
        modifier = modifier,
        onClickExport = { isExportSheetOpen = true },
        onClickDiagnose = { isDiagnoseSheetOpen = true },
        onUpdateBPMRange = statisticViewModel::updateDate,
        listBPMData = listBPMData,
        onClickSeeMoreCard = navToDetail,
        listOfReports = listOfReports,
        rangeDate = rangeDate,

    )

    if (isExportSheetOpen) {
        ModalBottomSheet(
            sheetState = exportSheetState,
            onDismissRequest = {
                isExportSheetOpen = false
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            ExportBottomSheet(
                onSubmit = { isExportSheetOpen = false }
            )
        }
    } else if (isDiagnoseSheetOpen) {
        ModalBottomSheet(
            sheetState = diagnoseSheetState,
            onDismissRequest = {
                isDiagnoseSheetOpen = false
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            DiagnoseBottomSheet(
                onSubmit = { isDiagnoseSheetOpen = false }
            )
        }
    }
}