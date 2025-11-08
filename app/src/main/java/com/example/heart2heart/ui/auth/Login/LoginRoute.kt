package com.example.heart2heart.ui.auth.Login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.heart2heart.auth.presentation.LoginViewModel

@Composable
fun LoginRoute(
    viewModel: LoginViewModel,
    onSignUpClicked: () -> Unit,
    onLoginSuccess: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkAuth()
    }

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            onLoginSuccess()
            viewModel.consumedLoginSuccess()
        }
    }

    LaunchedEffect(uiState.checkAuth) {
        if (uiState.checkAuth) {
            onLoginSuccess()
            viewModel.consumeCheckAuthSuccess()
        }
    }

    LoginScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginButtonClicked = viewModel::login,
        onSignUpClicked = onSignUpClicked
    )

}