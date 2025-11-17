package com.example.heart2heart

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.emptyCacheFontFamilyResolver
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.heart2heart.EmergencyBroadcast.presentation.EmergencyBroadcastViewModel
import com.example.heart2heart.EmergencyBroadcast.presentation.ReportViewModel
import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.auth.presentation.AuthCheckViewModel
import com.example.heart2heart.auth.presentation.LoginViewModel
import com.example.heart2heart.auth.presentation.RegisterViewModel
import com.example.heart2heart.bluetooth.BluetoothViewModel
import com.example.heart2heart.contacts.presentation.ContactViewModel
import com.example.heart2heart.home.presentation.HomeViewModel
import com.example.heart2heart.home.presentation.LocationViewModel
import com.example.heart2heart.report.presentation.StatisticViewModel
import com.example.heart2heart.ui.SOSPopup.OverlayReport
import com.example.heart2heart.ui.SOSPopup.OverlaySOS
import com.example.heart2heart.ui.auth.AuthCheckScreen
import com.example.heart2heart.ui.auth.Login.LoginScreen
import com.example.heart2heart.ui.auth.SignUp.SignUpScreen
import com.example.heart2heart.ui.auth.SignUp.SignupRouteScreen
import com.example.heart2heart.ui.contact.ContactScreen
import com.example.heart2heart.ui.contact.ContactScreenRoute
import com.example.heart2heart.ui.fab.FABTransition
import com.example.heart2heart.ui.home.HomeScreen
import com.example.heart2heart.ui.intro.ChooseContactObserverMode
import com.example.heart2heart.ui.intro.ChooseModeScreen
import com.example.heart2heart.ui.intro.components.ChooseModeScreenViewModel
import com.example.heart2heart.ui.maps.MapsScreen
import com.example.heart2heart.ui.navigation.BottomNavigationScreen
import com.example.heart2heart.ui.navigation.Screen
import com.example.heart2heart.ui.navigation.presentation.NavViewModel
import com.example.heart2heart.ui.report.ReportScreenRoute
import com.example.heart2heart.ui.setting.SettingScreen
import com.example.heart2heart.ui.statistic.StatisticScreen
import com.example.heart2heart.ui.statistic.StatisticScreenRoute
import com.example.heart2heart.ui.theme.Heart2HeartTheme
import com.example.heart2heart.utils.AuthCheckRoute
import com.example.heart2heart.utils.AuthRoute
import com.example.heart2heart.utils.ChooseModeScreenRoute
import com.example.heart2heart.utils.ChooseWhoToObserveRoute
import com.example.heart2heart.utils.ContactsRoute
import com.example.heart2heart.utils.HomeScreenRoute
import com.example.heart2heart.utils.LoginRoute
import com.example.heart2heart.utils.MainScreenRoute
import com.example.heart2heart.utils.MapScreenRoute
import com.example.heart2heart.utils.ReportResultScreenRoute
import com.example.heart2heart.utils.SettingsRoute
import com.example.heart2heart.utils.SignUpRoute
import com.example.heart2heart.utils.StatisticsRoute
import com.example.heart2heart.utils.askToEnableLocation
import com.example.heart2heart.utils.hasBackgroundLocationPermission
import com.example.heart2heart.utils.hasLocationPermission
import com.example.heart2heart.utils.isLocationEnabled
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import java.util.jar.Manifest

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val bluetoothManager by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val _isBluetoothEnabled: Boolean
        get() =  bluetoothAdapter?.isEnabled == true


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        /* activity */
        val enableBluetooth = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {

        }
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
                perms ->
            val canEnableBluetooth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                perms[android.Manifest.permission.BLUETOOTH_CONNECT] == true
            } else {
                true
            }

            if (canEnableBluetooth && !_isBluetoothEnabled) {
                enableBluetooth.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                )
            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.BLUETOOTH_SCAN,
                    android.Manifest.permission.BLUETOOTH_CONNECT,
                    android.Manifest.permission.FOREGROUND_SERVICE,
                )
            )
        }
        if (Build.VERSION.SDK_INT >= 33) {
            permissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.POST_NOTIFICATIONS,
                )
            )
        }
        if (Build.VERSION.SDK_INT >= 34) {
            permissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.FOREGROUND_SERVICE_DATA_SYNC,
                )
            )
        }

        FirebaseMessaging.getInstance().subscribeToTopic("SOS")
        FirebaseMessaging.getInstance().subscribeToTopic("report")

        setContent {
            Heart2HeartTheme {
                val context = LocalContext.current
                val activity = context as Activity
                /* Location */
                var locationPermissionsAlreadyGranted = ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                var shouldShowPermissionRationale by remember {
                    mutableStateOf(
                        shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    )
                }

                var permissionsGranted by remember { mutableStateOf(false) }
                val locationPermissionList = arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )

                val locationPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { results ->
                    permissionsGranted = results.values.all { it }
                }

                val locationEnableLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { result ->
                    locationPermissionsAlreadyGranted = result.values.reduce { acc, isPermissionGranted ->
                        acc && isPermissionGranted
                    }
                    if (!locationPermissionsAlreadyGranted) {
                        shouldShowPermissionRationale =
                            shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                    && shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)
                                    && shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    }
                }

                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(key1 = lifecycleOwner, effect = {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_START &&
                            !locationPermissionsAlreadyGranted &&
                            !shouldShowPermissionRationale
                        ) {
                            locationPermissionLauncher.launch(locationPermissionList)
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }
                )

                /* Location End */

                // SOS
                val emergencyViewModel = hiltViewModel<EmergencyBroadcastViewModel>()
                val openSOSDialog by emergencyViewModel.isRunningVibrate.collectAsState()
                val timeRemaining by emergencyViewModel.timeLeftSeconds.collectAsState()
                val emergencyState by emergencyViewModel.emergencyReportState.collectAsState()

                var isConnectionActive by remember { mutableStateOf(false) }

                // NAV
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val navViewModel = hiltViewModel<NavViewModel>()
                val appType by navViewModel.appType.collectAsState()

                /* Bluetooth */
                val bluetoothViewModel = hiltViewModel<BluetoothViewModel>()

                /* Auth */
                val authViewModel = hiltViewModel<AuthCheckViewModel>()

                Scaffold(
//                    floatingActionButton = {
//                        Box(
//                            modifier = Modifier
//                                .padding(bottom = 0.dp) // remove extra space
//                        ) {
//                            FABTransition(navBackStackEntry?.destination?.route)
//                        }
//                                            },
//                    floatingActionButtonPosition = FabPosition.End,
                    bottomBar = {
                        AnimatedVisibility(
                            visible = if( navBackStackEntry?.destination?.route == HomeScreenRoute::class.qualifiedName
                                || navBackStackEntry?.destination?.route == SettingsRoute::class.qualifiedName
                                || navBackStackEntry?.destination?.route == StatisticsRoute::class.qualifiedName
                                || navBackStackEntry?.destination?.route == ContactsRoute::class.qualifiedName
                            ) {
                                true
                            } else {
                                false
                            },
                            enter = slideInVertically(initialOffsetY = { it }),
                            exit = slideOutVertically(targetOffsetY = { it }),
                        ) {
                            BottomNavigationScreen(
                                screens = listOf(
                                    Screen(
                                        route = HomeScreenRoute,
                                        icon = R.drawable.home_ic,
                                        selectedIcon = R.drawable.home_ic,
                                    ),
                                    Screen(
                                        route = ContactsRoute,
                                        icon = R.drawable.person_ic,
                                        selectedIcon = R.drawable.person_ic,
                                    ),
                                    Screen(
                                        route = "SOS",
                                        icon = R.drawable.sos_ic,
                                        selectedIcon = R.drawable.sos_ic,
                                        altIcon = R.drawable.wifi_off
                                    ),

                                    Screen(
                                        route = StatisticsRoute,
                                        icon = R.drawable.chart_ic,
                                        selectedIcon = R.drawable.chart_ic,
                                    ),
                                    Screen(
                                        route = SettingsRoute,
                                        icon = R.drawable.setting_ic,
                                        selectedIcon = R.drawable.setting_ic,
                                    ),
                                ),
                                onNavigateTo = { navController.navigate(it.route) },
                                onEmergencyButton = {
                                    if (appType == AppType.OBSERVER) {
                                        if (isConnectionActive) {
                                            isConnectionActive = false
                                        } else {
                                            isConnectionActive = true
                                        }
                                    } else {
                                        emergencyViewModel.startWarningTimer()
                                    }
                                                    },
                                navController = navController,
                                appType = appType ?: AppType.AMBULATORY,
                                onStartListeningButton = { },
                                isConnected = isConnectionActive,
                            )
                        }
                    }
                ) {
                    innerPadding ->
                    OverlaySOS(
                        isOpenDialog = openSOSDialog,
                        onDismissRequest = { emergencyViewModel.cancelSOS() },
                        timeRemaining = timeRemaining,
                        senderOnReceiver = emergencyState.username,
                        isReceiverDialogOpen = emergencyState.isSOSObserverOpen,
                        onDismissReceiverDialog = { emergencyViewModel.confirmSOSAmbulatory() }
                    )
                    OverlayReport(
                        isOpenDialog = emergencyState.isOpenDialog,
                        detectionType = emergencyState.type,
                        senderOnReceiver = emergencyState.username,
                        onDismissRequest = { emergencyViewModel.confirmReport()}
                    )
                    NavHost(
                        navController = navController,
                        startDestination = AuthRoute,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(300)
                            )
                        },
                        exitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> -fullWidth },
                                animationSpec = tween(300)
                            )
                        },
                    ) {
                        composable<ChooseModeScreenRoute> {
                            val chooseModeScreenViewModel = hiltViewModel<ChooseModeScreenViewModel>()
                            ChooseModeScreen(
                                getProfile = chooseModeScreenViewModel::getUserData,
                                onClickObserver = {
                                    if (chooseModeScreenViewModel.uiState.value.profileSuccess) {
                                        chooseModeScreenViewModel.setAppType(AppType.OBSERVER)
                                        navController.navigate(ChooseWhoToObserveRoute)
                                    } else {
                                        chooseModeScreenViewModel.setAppType(AppType.OBSERVER)
                                        navController.navigate(ChooseWhoToObserveRoute)
                                    }
                                },
                                onClickAmbulatory = {
                                    if (chooseModeScreenViewModel.uiState.value.profileSuccess) {
                                        chooseModeScreenViewModel.setAppType(AppType.AMBULATORY)
                                        navController.navigate(MainScreenRoute)
                                    } else {
                                        chooseModeScreenViewModel.setAppType(AppType.AMBULATORY)
                                        navController.navigate(MainScreenRoute) {
                                            popUpTo(navController.graph.id) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                }
                            )
                        }

                        composable<ChooseWhoToObserveRoute> {
                            val chooseModeScreenViewModel = hiltViewModel<ChooseModeScreenViewModel>()
                            ChooseContactObserverMode(
                                modifier = Modifier.padding(innerPadding),
                                viewModel = chooseModeScreenViewModel,
                                onChooseWhoToObserve = {
                                    navController.navigate(MainScreenRoute) {
                                        popUpTo(navController.graph.id) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }

                        navigation<AuthRoute> (
                            startDestination = AuthCheckRoute,

                        ) {
                            composable<LoginRoute> {
                                val loginViewModel = hiltViewModel<LoginViewModel>()
                                com.example.heart2heart.ui.auth.Login.LoginRoute(
                                    loginViewModel,
                                    onSignUpClicked = {
                                        navController.navigate(SignUpRoute) {

                                        }
                                    },
                                    onLoginSuccess = {
                                        navController.navigate(ChooseModeScreenRoute) {
                                            popUpTo<AuthRoute> { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable<AuthCheckRoute> {
                                AuthCheckScreen(
                                    navController,
                                    Modifier.padding(innerPadding),
                                    authViewModel,
                                )
                            }
                            composable<SignUpRoute> {
                                val registerViewModel = hiltViewModel<RegisterViewModel>()
                                SignupRouteScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    viewModel = registerViewModel,
                                    onRegisterSuccess = {
                                        navController.navigate(LoginRoute)
                                    },
                                    onRouteToLoginClick = {
                                        navController.navigate(LoginRoute)
                                    }
                                )
                            }
                        }

                        navigation<MainScreenRoute> (
                            startDestination = HomeScreenRoute,
                            enterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { fullWidth -> fullWidth },
                                    animationSpec = tween(300)
                                )
                            },
                            exitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { fullWidth -> -fullWidth },
                                    animationSpec = tween(300)
                                )
                            },
                        ) {
                            composable<HomeScreenRoute> {
                                val homeViewModel = hiltViewModel<HomeViewModel>()
                                HomeScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    bluetoothViewModel = bluetoothViewModel,
                                    navController = navController,
                                    homeViewModel = homeViewModel,
                                    appType = appType ?: AppType.AMBULATORY,
                                )
                            }

                            composable<SettingsRoute> {
                                SettingScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    onLogoutClicked = {
                                        authViewModel.logOut()
                                        navController.navigate(AuthRoute)
                                        {
                                            popUpTo(navController.graph.id) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                )
                            }

                            composable<ContactsRoute> {
                                val contactViewModel = hiltViewModel<ContactViewModel>()
                                ContactScreenRoute(modifier = Modifier.padding(innerPadding), contactViewModel, appType = appType ?: AppType.AMBULATORY)
                            }

                            composable<StatisticsRoute> {
                                val statisticView = hiltViewModel<StatisticViewModel>()
                                StatisticScreenRoute(
                                    modifier = Modifier.padding(innerPadding),
                                    statisticView,
                                    navToDetail = {
                                            id ->
                                        navController.navigate(ReportResultScreenRoute(id))
                                    }
                                )
                            }

                            composable<ReportResultScreenRoute> {
                                    backStackEntry ->
                                val reportResultData = backStackEntry.toRoute<ReportResultScreenRoute>()
                                val reportViewModel = hiltViewModel<ReportViewModel>()
                                ReportScreenRoute(
                                    modifier = Modifier.padding(innerPadding),
                                    reportId = reportResultData.reportId,
                                    onBackPressed = { navController.popBackStack() },
                                    reportViewModel = reportViewModel,
                                )
                            }

                            composable<MapScreenRoute> {
                                val locViewModel = hiltViewModel<LocationViewModel>()
                                MapsScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    viewModel = locViewModel,
                                )
                            }

                        }
                    }

                }

            }
        }
    }
}