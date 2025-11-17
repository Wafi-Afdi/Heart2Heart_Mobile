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
        end = LocalDateTime.now()
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

    init {
        viewModelScope.launch {
            statisticRepository.toastMessage.collect {
                message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                delay(2000)
            }
        }

        viewModelScope.launch {
            statisticRepository.fetchReportList()
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
            statisticRepository.fetchReportList()
        }
        viewModelScope.launch {
            val endDay = LocalDate.now()
            val endOfDayTime =  endDay.atStartOfDay()

            val startDay = endDay.minusDays(6)
            val startOfDay =  startDay.atStartOfDay()
            statisticRepository.fetchBPMData(startOfDay, endOfDayTime)
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