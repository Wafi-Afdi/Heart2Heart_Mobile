package com.example.heart2heart.report.presentation

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heart2heart.auth.presentation.LoginUIState
import com.example.heart2heart.report.data.repository.StatisticRepository
import com.example.heart2heart.report.presentation.state.rangeDate
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val statisticRepository: StatisticRepository
): ViewModel() {

    val listOfReports = statisticRepository.listOfReports

    val listOfChartBPM = statisticRepository.listOfAverage

    private val _rangeDateBPM = MutableStateFlow(rangeDate(
        start = LocalDateTime.now().minusDays(6),
        end = LocalDateTime.now(),
        isLoading = false
    ))
    val rangeDateBPM = _rangeDateBPM.asStateFlow()

    fun updateDate(start: LocalDateTime, end: LocalDateTime) {
        _rangeDateBPM.update {
            it.copy(
                start,
                end
            )
        }
        changeDate(_rangeDateBPM.value.start, _rangeDateBPM.value.end)
    }

    private val _isProcessingCSV = MutableStateFlow(false)
    val isProcessingCSV = _isProcessingCSV.asStateFlow()

    private val _isLoadingReport = MutableStateFlow(false)
    val isLoadingReport = _isLoadingReport.asStateFlow()

    private val _isLoadingDiagnosis = MutableStateFlow(false)
    val isLoadingDiagnosis = _isLoadingDiagnosis.asStateFlow()

    init {
        viewModelScope.launch {
            statisticRepository.toastMessage.collect {
                message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                delay(2000)
            }
        }

        viewModelScope.launch {
            _isLoadingReport.update { true }
            statisticRepository.fetchReportList()
            _isLoadingReport.update { false }
        }
        viewModelScope.launch {
            updateDate(
                start = LocalDateTime.now().minusDays(6),
                end = LocalDateTime.now()
            )
        }
    }

    fun onRefreshData() {
        viewModelScope.launch {
            _isLoadingReport.update { true }
            statisticRepository.fetchReportList()
            _isLoadingReport.update { false }
        }
        viewModelScope.launch {
            _rangeDateBPM.update { it.copy(isLoading = true) }
            statisticRepository.fetchBPMData(_rangeDateBPM.value.start, _rangeDateBPM.value.end)

            _rangeDateBPM.update { it.copy(isLoading = false) }
        }
    }

    fun generateDiagnosis() {
        viewModelScope.launch {
            _isLoadingDiagnosis.update { true }
            statisticRepository.generateDiagnosis(LocalDateTime.now())
            _isLoadingDiagnosis.update { false }
        }
    }

    fun exportData(start: LocalDateTime, end: LocalDateTime) {
        viewModelScope.launch {
            _isProcessingCSV.update { true }
            statisticRepository.exportECG(start, end)
            _isProcessingCSV.update { false }
        }
    }

    fun changeDate(start: LocalDateTime, end: LocalDateTime) {
        viewModelScope.launch {
            _rangeDateBPM.update { it.copy(isLoading = true) }
            statisticRepository.fetchBPMData(start, end)
            _rangeDateBPM.update { it.copy(isLoading = false) }
        }
    }

}