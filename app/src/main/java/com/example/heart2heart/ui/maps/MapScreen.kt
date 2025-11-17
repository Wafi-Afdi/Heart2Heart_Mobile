package com.example.heart2heart.ui.maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.heart2heart.home.presentation.LocationViewModel
import com.example.heart2heart.utils.PersonCircle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@Composable
fun MapsScreen(
    modifier: Modifier = Modifier,
    viewModel: LocationViewModel,
) {
    val locationState by viewModel.locationState.collectAsState()

    val ecgUserLocation = LatLng(locationState.latitude, locationState.longitude)
    val ecgUserLocationMarkerState = rememberUpdatedMarkerState(position = ecgUserLocation)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ecgUserLocation, 15f)
    }
    Box(modifier = modifier.fillMaxSize()) {
        // Map itself
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = ecgUserLocationMarkerState,
                title = "ECG User",
                snippet = "ECG user location"
            )
        }

        FloatingActionButton(
            onClick = {
                // Animate the map camera back to your location
                cameraPositionState.move(
                    CameraUpdateFactory.newLatLngZoom(ecgUserLocation, 15f)
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
            ,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            Icon(
                imageVector = PersonCircle,
                contentDescription = "Go to ECG User Location",
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}