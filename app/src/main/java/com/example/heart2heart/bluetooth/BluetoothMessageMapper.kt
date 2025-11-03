package com.example.heart2heart.bluetooth

import java.time.LocalDateTime

fun parseToBluetoothECGData(message: String, time: LocalDateTime): BluetoothSTMECGData? {
    return BluetoothSTMECGData(
        ecgPoint = 0.0,
        qrsFlag = false,
        asystoleFlag = false,
        timeStamp = time,
    )
}