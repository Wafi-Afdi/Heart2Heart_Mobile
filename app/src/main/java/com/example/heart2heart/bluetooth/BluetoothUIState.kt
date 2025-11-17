package com.example.heart2heart.bluetooth

data class BluetoothUIState(
    val scannedDevices: List<BluetoothDataModel> = emptyList(),
    val pairedDevices: List<BluetoothDataModel> = emptyList(),
    val deviceConnectedTo: String = "No Connection",
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,

)
