package com.example.heart2heart.bluetooth

import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {
    val scannedDevices: StateFlow<List<BluetoothDataModel>>
    val pairedDevices: StateFlow<List<BluetoothDataModel>>

    fun startDiscovery()
    fun stopDiscovery()

    fun release()
}