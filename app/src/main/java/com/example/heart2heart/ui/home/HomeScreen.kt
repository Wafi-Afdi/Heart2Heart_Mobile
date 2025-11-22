package com.example.heart2heart.ui.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.heart2heart.R
import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.bluetooth.BluetoothUIState
import com.example.heart2heart.bluetooth.BluetoothViewModel
import com.example.heart2heart.home.presentation.HomeViewModel
import com.example.heart2heart.ui.home.components.TitleView.TitleView
import com.example.heart2heart.ui.home.components.chart.ChartView
import com.example.heart2heart.ui.home.components.contacts.ContactView
import com.example.heart2heart.ui.home.components.deviceConnection.DeviceInfoView
import com.example.heart2heart.ui.home.components.location.LocationView
import com.example.heart2heart.ui.home.components.report.ReportView
import com.example.heart2heart.ui.navigation.BottomNavigationScreen
import com.example.heart2heart.ui.navigation.Screen
import com.example.heart2heart.ui.theme.ubuntuFamily
import com.example.heart2heart.utils.AlignEndHorizontal
import com.example.heart2heart.utils.ChooseModeScreenRoute
import com.example.heart2heart.utils.MapScreenRoute
import com.example.heart2heart.utils.PreviewWrapperWithScaffold
import com.example.heart2heart.utils.ReportResultScreenRoute
import com.example.heart2heart.utils.StatisticsRoute
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    bluetoothViewModel: BluetoothViewModel,
    navController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel,
    appType: AppType
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    val bluetoothViewModelState by bluetoothViewModel.state.collectAsState()
    val userHomeState by homeViewModel.userHomeState.collectAsState()
    val lastConnectionTimeBluetooth by bluetoothViewModel.lastConnectedBluetooth.collectAsState()
    val isObserverBluetoothOn by homeViewModel.isObserverBluetoothConnected.collectAsState()

    val connectedUser by homeViewModel.totalPersonWatching.collectAsState()

    val isDeviceConnected = (
            if(appType == AppType.AMBULATORY) {
                bluetoothViewModelState.isConnected
            } else {
                isObserverBluetoothOn
            }
            )

    val heartBPM by homeViewModel.heartBPM.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }

    val locationState by homeViewModel.locationState.collectAsState()

    val listOfReport by homeViewModel.listOfReports.collectAsState()

    val appContext = LocalContext.current.applicationContext

    LaunchedEffect(bluetoothViewModelState.errorMessage) {
        bluetoothViewModelState.errorMessage?.let { message ->
            Toast.makeText(
                appContext,
                message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(bluetoothViewModelState.isConnected) {
        if (bluetoothViewModelState.isConnected) {
            Toast.makeText(
                appContext,
                "Connected",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.startTracking()
        homeViewModel.fetchReportList()
    }

    Box(modifier = modifier.fillMaxSize()){
        if(bluetoothViewModelState.isConnecting) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Text(
                    text = "Connecting"
                )
            }
        }
        else {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                TitleView(
                    onClickSetting = {
                        navController.navigate(ChooseModeScreenRoute)
                    },
                    name = userHomeState.name,
                    appType = userHomeState.appType
                )
                Spacer(Modifier.height(8.dp))
                ChartView(
                    isConnected = isDeviceConnected,
                    userBeingViewed = (
                        if (userHomeState.appType == AppType.AMBULATORY) {
                            userHomeState.name
                        } else if (userHomeState.appType == AppType.OBSERVER) {
                            userHomeState.userBeingMonitored?.name
                        } else {
                            "Unknown"
                        }
                    )
                )
                Spacer(Modifier.height(8.dp))
                DeviceInfoView(
                    isConnected = isDeviceConnected,
                    onDeviceButtonClick = {
                        if (appType == AppType.AMBULATORY) {
                            isSheetOpen = true
                            bluetoothViewModel.startScan()
                        }
                    },
                    disconnectDevice = {
                        bluetoothViewModel.disconnectFromDevice()
                    },
                    lastConnectionTime = lastConnectionTimeBluetooth ?: LocalDateTime.now(),
                    deviceName = bluetoothViewModelState.deviceConnectedTo,
                    bpm = heartBPM,
                    appType = appType,
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                    ,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LocationView(
                        modifier = Modifier.weight(1f),
                        onCardClick = {
                            navController.navigate(
                                MapScreenRoute
                            )
                        },
                        long = locationState.longitude,
                        lat = locationState.latitude,
                        nation = locationState.country ?: "-",
                        provinceState = locationState.city ?: "-"

                    )
                    Spacer(Modifier.width(8.dp))
                    ContactView(
                        modifier = Modifier.weight(1f),
                        totalPerson = connectedUser.size,
                        userConnected = connectedUser.values.toList(),
                    )
                }
                Spacer(Modifier.height(16.dp))
                ReportView(
                    onClickSeeMoreTop = {
                        navController.navigate(StatisticsRoute)
                    },
                    onClickSeeMoreCard = {
                        id ->
                        navController.navigate(ReportResultScreenRoute(id))
                    },
                    listOfReport = listOfReport,
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }

    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                isSheetOpen = false
                bluetoothViewModel.stopScan()
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            LazyColumn(
                modifier = Modifier.padding(8.dp)
            ) {
                items(bluetoothViewModelState.pairedDevices) {
                        devices ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(vertical = 8.dp, horizontal = 8.dp)
                            .clickable {
                                bluetoothViewModel.connectToDevice(devices)
                                isSheetOpen = false
                            }
                    ) {
                        Text(
                            text = devices.name ?: "No Name",
                            style = LocalTextStyle.current.copy(
                                lineHeight = 16.sp
                            ),
                            fontFamily = ubuntuFamily,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.white),
                            fontSize = 16.sp,
                        )
                    }
                    HorizontalDivider(color = colorResource(
                        R.color.accent_dark),
                        thickness = 2.dp,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                    )
                }
                items(bluetoothViewModelState.scannedDevices) {
                        devices ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(vertical = 8.dp, horizontal = 8.dp)
                            .clickable {
                                bluetoothViewModel.connectToDevice(devices)
                                isSheetOpen = false
                            }
                    ) {
                        Text(
                            text = devices.name ?: "No Name",
                            style = LocalTextStyle.current.copy(
                                lineHeight = 16.sp
                            ),
                            fontFamily = ubuntuFamily,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.white),
                            fontSize = 16.sp,
                        )
                    }
                    HorizontalDivider(color = colorResource(
                        R.color.accent_dark),
                        thickness = 2.dp,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true, name = "My Component in a Scaffold")
//@Composable
//fun MyIsolatedComponentPreview() {
//    val bluetoothViewModel = hiltViewModel<BluetoothViewModel>()
//    PreviewWrapperWithScaffold { paddingValues ->
//        // Call your isolated component inside the wrapper's content lambda,
//        // using the padding provided by the Scaffold.
//        HomeScreen(modifier = Modifier.padding(paddingValues), bluetoothViewModel)
//    }
//}