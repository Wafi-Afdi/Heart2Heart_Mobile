package com.example.heart2heart.bluetooth

sealed interface ConnectionResult {
    object ConnectionEstablished : ConnectionResult
    data class Error(val message: String) : ConnectionResult

    data class TransferSucceeded(val message: String): ConnectionResult

}