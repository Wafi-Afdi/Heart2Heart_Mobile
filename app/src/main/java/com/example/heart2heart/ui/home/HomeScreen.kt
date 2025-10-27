package com.example.heart2heart.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.heart2heart.R
import com.example.heart2heart.ui.home.components.TitleView.TitleView
import com.example.heart2heart.ui.home.components.chart.ChartView
import com.example.heart2heart.ui.home.components.contacts.ContactView
import com.example.heart2heart.ui.home.components.deviceConnection.DeviceInfoView
import com.example.heart2heart.ui.home.components.location.LocationView
import com.example.heart2heart.ui.home.components.report.ReportView
import com.example.heart2heart.ui.navigation.BottomNavigationScreen
import com.example.heart2heart.ui.navigation.Screen
import com.example.heart2heart.utils.PreviewWrapperWithScaffold

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        TitleView()
        Spacer(Modifier.height(8.dp))
        ChartView()
        Spacer(Modifier.height(8.dp))
        DeviceInfoView()
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
            ,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LocationView(modifier = Modifier.weight(1f))
            Spacer(Modifier.width(8.dp))
            ContactView(modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(16.dp))
        ReportView()
        Spacer(Modifier.height(8.dp))
    }
}

@Preview(showBackground = true, name = "My Component in a Scaffold")
@Composable
fun MyIsolatedComponentPreview() {

    PreviewWrapperWithScaffold { paddingValues ->
        // Call your isolated component inside the wrapper's content lambda,
        // using the padding provided by the Scaffold.
        HomeScreen(modifier = Modifier.padding(paddingValues))
    }
}