package com.example.heart2heart.ui.navigation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.heart2heart.R
import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.utils.ContactsRoute
import com.example.heart2heart.utils.HomeScreenRoute
import com.example.heart2heart.utils.MainScreenRoute
import com.example.heart2heart.utils.SettingsRoute
import com.example.heart2heart.utils.StatisticsRoute
import com.example.heart2heart.utils.TriangleRight

@Composable
private fun RowScope.BottomBarItem(
    screen: Screen,
    onNavigateTo: (Screen) -> Unit,
    isActive: Boolean = false,
    NotFAB: Boolean = true,
    appType: AppType = AppType.AMBULATORY,
    isConnectionActive: Boolean = false,
) {

    Box(
        Modifier
            .selectable(
                selected = isActive,
                onClick = {
                    if (!isActive) {
                        onNavigateTo(screen)
                    }
                },
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = remember { ripple(radius = 32.dp) }
            )
            .fillMaxHeight()
            .weight(1f)

            .background(
                if(!NotFAB && appType == AppType.OBSERVER) {
                    if (isConnectionActive) {
                        colorResource(R.color.success)
                    } else {
                        colorResource(R.color.neutral_700)
                    }
                }
                else if (!NotFAB) {
                    MaterialTheme.colorScheme.primary
                }else {
                    Color.Transparent
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        BadgedBox(
            badge = {},
            content = {
                Icon(
                    painter = painterResource(
                        id = (if(!NotFAB && appType == AppType.OBSERVER && screen.altIcon != null) {
                            if (isConnectionActive) R.drawable.wifi_on
                                else screen.altIcon
                        }

                            else screen.selectedIcon
                                )
                    ),
                    contentDescription = null,
                    tint = if (appType == AppType.OBSERVER && !NotFAB) {
                        if (isConnectionActive) {
                            colorResource(R.color.text_dark)
                        } else {
                            colorResource(R.color.neutral_900)
                        }
                    } else if(isActive && NotFAB) {
                        MaterialTheme.colorScheme.primary
                    } else if (!NotFAB) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                    ,
                    modifier = Modifier.size(32 .dp),
                )
            },
        )
    }
}

val bottomNavDestination = listOf(HomeScreenRoute, StatisticsRoute, ContactsRoute, SettingsRoute)

@Composable
fun BottomNavigationScreen(
    modifier: Modifier = Modifier,
    screens: List<Screen>,
    onNavigateTo: (Screen) -> Unit,
    onEmergencyButton: (Screen) -> Unit,
    navController: NavHostController,
    appType: AppType = AppType.AMBULATORY,
    onStartListeningButton: () -> Unit = { },
    isConnected: Boolean = false,
) {
    val backgroundShape = remember { menuBarShape() }

    val entry by navController.currentBackStackEntryAsState()
    val currentDestination = entry?.destination

    val getIsActiveScreen = { screen: Screen -> currentDestination?.hierarchy?.any {
        it.route == screen.route::class.qualifiedName
    } == true }

    Box(
        modifier = Modifier
        .navigationBarsPadding()
        .background(Color.Transparent)

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.BottomCenter)

                .background(MaterialTheme.colorScheme.surface, backgroundShape)
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
        ) {
            FloatingActionButton(
                shape = RoundedCornerShape(50),
                containerColor = colorResource(R.color.primary_light),
                contentColor = Color.Gray,
                onClick = {},
                modifier = Modifier.clip(RoundedCornerShape(50))
            ) {
                Row(
                    modifier = Modifier.size(64.dp)
                ) {
                    BottomBarItem(screens[2], onEmergencyButton, false, NotFAB = false, appType = appType, isConnectionActive = isConnected)
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }

        Row(
            modifier = Modifier
                .height(56.dp)
                .align(Alignment.BottomCenter)
        ) {
            BottomBarItem(screens[0], onNavigateTo, getIsActiveScreen(screens[0]))
            BottomBarItem(screens[1], onNavigateTo, getIsActiveScreen(screens[1]))

            Spacer(modifier = Modifier.width(72.dp))

            BottomBarItem(screens[3], onNavigateTo, getIsActiveScreen(screens[3]))
            BottomBarItem(screens[4], onNavigateTo, getIsActiveScreen(screens[4]))
        }
    }

}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    var currentScreen by remember { mutableStateOf<Screen?>(null) }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        BottomNavigationScreen(
            screens = listOf(
                Screen(
                    route = "home",
                    icon = R.drawable.home_ic,
                    selectedIcon = R.drawable.home_ic,
                ),
                Screen(
                    route = "contacts",
                    icon = R.drawable.person_ic,
                    selectedIcon = R.drawable.person_ic,
                ),
                Screen(
                    route = "SOS",
                    icon = R.drawable.sos_ic,
                    selectedIcon = R.drawable.sos_ic,
                    altIcon = R.drawable.wifi_off,
                ),
                Screen(
                    route = "profile",
                    icon = R.drawable.chart_ic,
                    selectedIcon = R.drawable.chart_ic,
                ),
                Screen(
                    route = "chat",
                    icon = R.drawable.setting_ic,
                    selectedIcon = R.drawable.setting_ic,
                ),
            ),
            onNavigateTo = {  currentScreen = it },
            onEmergencyButton = {  currentScreen = it },
            navController = NavHostController(LocalContext.current)
        )
    }
}