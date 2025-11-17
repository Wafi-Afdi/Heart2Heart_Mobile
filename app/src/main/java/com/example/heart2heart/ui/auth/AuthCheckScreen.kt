package com.example.heart2heart.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.heart2heart.R
import com.example.heart2heart.auth.domain.AuthState
import com.example.heart2heart.auth.presentation.AuthCheckViewModel
import com.example.heart2heart.utils.AuthCheckRoute
import com.example.heart2heart.utils.AuthRoute
import com.example.heart2heart.utils.ChooseModeScreenRoute
import com.example.heart2heart.utils.LoginRoute

@Composable
fun AuthCheckScreen(
    navController: NavController,
    modifier: Modifier,
    authCheckViewModel: AuthCheckViewModel,
) {

    val authState by authCheckViewModel.authState.collectAsState()

    LaunchedEffect(Unit) {
        authCheckViewModel.checkAuth()
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate(ChooseModeScreenRoute) {
                    popUpTo(AuthRoute) { inclusive = true }
                }
            }
            is AuthState.Unauthenticated -> {
                navController.navigate(LoginRoute) {
                    popUpTo(AuthCheckRoute) { inclusive = true }
                }
            }
            is AuthState.Loading -> {
                // Do nothing
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize().background(colorResource(R.color.primary_dark)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.heart2heart_logo),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
        )
    }
}