package com.example.heart2heart.home.presentation

import androidx.lifecycle.ViewModel
import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.auth.repository.ProfileRepository
import com.example.heart2heart.home.domain.LiveLocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val profileRepo: ProfileRepository,
    private val liveLocationService: LiveLocationService
): ViewModel() {

    init {
        if (profileRepo.appType.value == AppType.AMBULATORY) {
            startTracking()
        }
    }
    val locationState = liveLocationService.locationState

    fun startTracking() {
        liveLocationService.startLocationUpdates()
    }

    fun stopTracking() {
        liveLocationService.stopLocationUpdates()
    }
}