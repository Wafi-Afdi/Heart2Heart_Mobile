package com.example.heart2heart.bluetooth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {
    val isConnected: StateFlow<Boolean>
    val scannedDevices: StateFlow<List<BluetoothDataModel>>
    val pairedDevices: StateFlow<List<BluetoothDataModel>>
    val errors: SharedFlow<String>


    suspend fun trySendMessagePing(message: String)

    fun startDiscovery()
    fun stopDiscovery()

    fun startBluetoothServer(): Flow<ConnectionResult>
    fun connectToDevice(device: BluetoothDataModel): Flow<ConnectionResult>
    fun closeConnection()
    fun release()
}