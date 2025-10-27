package com.example.heart2heart.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.example.heart2heart.bluetooth.BluetoothDeviceDomain
@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDataModel {
    return BluetoothDataModel(
        name = name,
        address = address
    )
}