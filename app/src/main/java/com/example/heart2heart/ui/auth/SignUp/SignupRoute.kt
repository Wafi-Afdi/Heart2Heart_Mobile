package com.example.heart2heart.ui.auth.SignUp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.heart2heart.auth.presentation.RegisterViewModel

@Composable
fun SignupRouteScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel,
    onRouteToLoginClick: () -> Unit,
    onRegisterSuccess: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.registerSuccess) {
        if (uiState.registerSuccess) {
            onRegisterSuccess()
            viewModel.consumedRegisterSuccess()
        }
    }

    SignUpScreen(
        modifier,
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onPhoneChange = viewModel::onPhoneUpdate,
        onFullNameChange = viewModel::onFullNameUpdate,
        onLoginClicked = onRouteToLoginClick,
        onRegisterClick = viewModel::register
    )

}