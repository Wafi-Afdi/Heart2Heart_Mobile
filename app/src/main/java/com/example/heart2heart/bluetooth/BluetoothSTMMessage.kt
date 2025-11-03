package com.example.heart2heart.bluetooth

import java.time.LocalDateTime

data class BluetoothSTMECGData(
    val ecgPoint: Double,
    val qrsFlag: Boolean,
    val asystoleFlag: Boolean,
    val timeStamp: LocalDateTime?
)
