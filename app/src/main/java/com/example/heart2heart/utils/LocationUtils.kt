package com.example.heart2heart.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.activity.result.IntentSenderRequest
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority

fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

fun hasLocationPermission(context: Context): Boolean {
    return ActivityCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}

fun hasBackgroundLocationPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= 29) {
        ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    else true
}

fun askToEnableLocation(activity: Activity, launcher: (IntentSenderRequest) -> Unit) {
    val locationRequest = LocationRequest.Builder(1000)
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .build()

    val builder = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)
        .setAlwaysShow(true)

    val client = LocationServices.getSettingsClient(activity)
    val task = client.checkLocationSettings(builder.build())

    task.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            try {
                val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                launcher(intentSenderRequest)
            } catch (sendEx: IntentSender.SendIntentException) {
                sendEx.printStackTrace()
            }
        }
    }
}