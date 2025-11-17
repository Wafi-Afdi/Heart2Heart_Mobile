package com.example.heart2heart.ui.navigation.presentation

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.auth.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    private val profileRepo: ProfileRepository,
): ViewModel() {

    val appType: StateFlow<AppType?>
        get() = profileRepo.appType
}