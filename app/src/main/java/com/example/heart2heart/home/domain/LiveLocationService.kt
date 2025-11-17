package com.example.heart2heart.home.domain

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import com.example.heart2heart.auth.data.remote.AuthAPI
import com.example.heart2heart.home.data.LocationData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

class LiveLocationService @Inject constructor(
    @ApplicationContext private val context: Context
)
{
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val _locationState = MutableStateFlow(LocationData())
    val locationState = _locationState.asStateFlow()

    private var locationCallback: LocationCallback? = null
    private var trackingJob: Job? = null

    private val scope = CoroutineScope(Dispatchers.IO)

    @SuppressLint("MissingPermission") // make sure permissions handled outside
    fun startLocationUpdates() {
        if (locationCallback != null) return // already running

        val request = LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            5000L // 5 seconds
        ).setMinUpdateIntervalMillis(5000L)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation ?: return
                scope.launch {
                    _locationState.value = LocationData.fromLocation(location)
                    val (city, country) = getCityAndCountry(context, location.latitude, location.longitude)
                    _locationState.value.city = city
                    _locationState.value.country = country
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            request,
            locationCallback!!,
            null
        )
    }

    fun stopLocationUpdates() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
        locationCallback = null
        trackingJob?.cancel()
        trackingJob = null
    }

    fun setLocationData(loc: Location) {
        _locationState.value = LocationData.fromLocation(loc)
    }

    fun getCityAndCountry(context: Context, lat: Double, lon: Double): Pair<String?, String?> {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (!addresses.isNullOrEmpty()) {
                val city = addresses[0].locality
                val country = addresses[0].countryName
                city to country
            } else {
                null to null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null to null
        }
    }
}