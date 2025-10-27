package com.example.heart2heart.bluetooth

import android.bluetooth.BluetoothDevice

typealias BluetoothDeviceDomain = BluetoothDevice

data class BluetoothDataModel(
    val name: String?,
    val address: String
)
