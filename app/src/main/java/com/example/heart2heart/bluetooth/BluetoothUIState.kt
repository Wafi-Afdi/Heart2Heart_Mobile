package com.example.heart2heart.bluetooth

data class BluetoothUIState(
    val scannedDevices: List<BluetoothDataModel> = emptyList(),
    val pairedDevices: List<BluetoothDataModel> = emptyList(),

)
