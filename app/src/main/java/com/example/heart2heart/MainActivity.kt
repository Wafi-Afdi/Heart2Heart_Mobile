package com.example.heart2heart

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.auth.presentation.LoginViewModel
import com.example.heart2heart.auth.presentation.RegisterViewModel
import com.example.heart2heart.bluetooth.BluetoothViewModel
import com.example.heart2heart.home.presentation.HomeViewModel
import com.example.heart2heart.ui.auth.Login.LoginScreen
import com.example.heart2heart.ui.auth.SignUp.SignUpScreen
import com.example.heart2heart.ui.auth.SignUp.SignupRouteScreen
import com.example.heart2heart.ui.contact.ContactScreen
import com.example.heart2heart.ui.contact.ContactScreenRoute
import com.example.heart2heart.ui.home.HomeScreen
import com.example.heart2heart.ui.intro.ChooseModeScreen
import com.example.heart2heart.ui.intro.components.ChooseModeScreenViewModel
import com.example.heart2heart.ui.navigation.BottomNavigationScreen
import com.example.heart2heart.ui.navigation.Screen
import com.example.heart2heart.ui.setting.SettingScreen
import com.example.heart2heart.ui.statistic.StatisticScreen
import com.example.heart2heart.ui.theme.Heart2HeartTheme
import com.example.heart2heart.utils.AuthRoute
import com.example.heart2heart.utils.ChooseModeScreenRoute
import com.example.heart2heart.utils.ContactsRoute
import com.example.heart2heart.utils.HomeScreenRoute
import com.example.heart2heart.utils.LoginRoute
import com.example.heart2heart.utils.MainScreenRoute
import com.example.heart2heart.utils.SettingsRoute
import com.example.heart2heart.utils.SignUpRoute
import com.example.heart2heart.utils.StatisticsRoute
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

        setContent {
            Heart2HeartTheme {

                var currentScreen by remember { mutableStateOf<Screen?>(null) }
                val navController = rememberNavController()

                val navBackStackEntry by navController.currentBackStackEntryAsState()

                /* Bluetooth */
                val bluetoothViewModel = hiltViewModel<BluetoothViewModel>()

                LaunchedEffect(true) {

                }

                Scaffold(
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
                                onEmergencyButton = { },
                                navController = navController
                            )
                        }
                    }
                ) {
                    innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = MainScreenRoute,
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
                                        navController.navigate(MainScreenRoute)
                                    }
                                },
                                onClickAmbulatory = {
                                    if (chooseModeScreenViewModel.uiState.value.profileSuccess) {
                                        chooseModeScreenViewModel.setAppType(AppType.AMBULATORY)
                                        navController.navigate(MainScreenRoute)
                                    }
                                }
                            )
                        }

                        navigation<AuthRoute> (
                            startDestination = LoginRoute,

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
                                    homeViewModel = homeViewModel
                                )
                            }

                            composable<SettingsRoute> {
                                SettingScreen()
                            }

                            composable<ContactsRoute> {
                                ContactScreenRoute(modifier = Modifier.padding(innerPadding))
                            }

                            composable<StatisticsRoute> {
                                StatisticScreen()
                            }

                        }
                    }

                }

            }
        }
    }
}