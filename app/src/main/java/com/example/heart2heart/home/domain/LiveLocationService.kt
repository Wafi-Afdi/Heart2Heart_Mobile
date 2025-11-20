package com.example.heart2heart.home.domain

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.auth.data.remote.AuthAPI
import com.example.heart2heart.auth.repository.ProfileRepository
import com.example.heart2heart.home.data.LocationData
import com.example.heart2heart.websocket.data.dto.LocationDTO
import com.example.heart2heart.websocket.repository.WebSocketRepository
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Locale
import javax.inject.Inject

class LiveLocationService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val profileRepository: ProfileRepository,
    private val webSocketRepository: WebSocketRepository
)
{
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val _locationState = MutableStateFlow(LocationData())
    val locationState = _locationState.asStateFlow()

    private var locationCallback: LocationCallback? = null
    private var trackingJob = Job()

    private val scope = CoroutineScope(Dispatchers.IO + trackingJob)

    private var locationDto: LocationDTO = LocationDTO(
        lat = 0.0f,
        longitude = 0.0f,
        country = "Indonesia",
        city = "-"
    )

    @SuppressLint("MissingPermission") // make sure permissions handled outside
    fun startLocationUpdates() {
        if (locationCallback != null) return // already running

        if (profileRepository.appType.value == AppType.AMBULATORY) {
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

                        locationDto.country = _locationState.value.country ?: "Indonesia"
                        locationDto.city = _locationState.value.city ?: "-"
                        locationDto.lat = _locationState.value.latitude.toFloat()
                        locationDto.longitude = _locationState.value.longitude.toFloat()

                        webSocketRepository.publish("/app/location", Json.encodeToString(locationDto))
                    }
                }
            }


            fusedLocationClient.requestLocationUpdates(
                request,
                locationCallback!!,
                null
            )
        } else {
            scope.launch {
                webSocketRepository.locationFlow.collect {
                    loc ->
                    _locationState.update {
                        it.copy(
                            latitude = loc.lat.toDouble(),
                            longitude = loc.longitude.toDouble(),
                            city = loc.city,
                            country = loc.country,
                        )
                    }
                }
            }
        }

    }

    fun stopLocationUpdates() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
        locationCallback = null
        trackingJob.cancel()
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