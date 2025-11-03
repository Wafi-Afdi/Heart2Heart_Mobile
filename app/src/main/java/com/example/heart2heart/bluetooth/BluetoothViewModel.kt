package com.example.heart2heart.bluetooth

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class BluetoothViewModel @Inject constructor(
    private val bluetoothController: BluetoothServiceECG
): ViewModel() {
    private val _state = MutableStateFlow(BluetoothUIState())
    val state = combine(
        bluetoothController.scannedDevices,
        bluetoothController.pairedDevices,
        _state
    ) { scannedDevices, pairedDevices, state ->
        state.copy(
            scannedDevices = scannedDevices,
            pairedDevices = pairedDevices
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    private var deviceConnectionJob: Job? = null


    init {
        bluetoothController.isConnected.onEach {
            _state.update { it.copy(isConnected = it.isConnected) }
        }.launchIn(viewModelScope)

        bluetoothController.errors.onEach {
            error ->
            _state.update {
                it.copy(
                    errorMessage = error
                )
            }
        }.launchIn(viewModelScope)
    }

    @SuppressLint("MissingPermission")
    fun startScan() {
        bluetoothController.startDiscovery()
    }

    fun stopScan() {
        bluetoothController.stopDiscovery()
    }

    fun connectToDevice(device: BluetoothDataModel) {
        _state.update {
            it.copy(isConnecting = true)
        }
        deviceConnectionJob = bluetoothController
            .connectToDevice(device)
            .listen()
    }

    fun disconnectFromDevice() {
        deviceConnectionJob?.cancel()
        bluetoothController.closeConnection()
        _state.update {
            it.copy(
                isConnecting = false,
                isConnected = false
            )
        }
    }

    fun waitForIncomingConnection() {
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = bluetoothController
            .startBluetoothServer()
            .listen()
    }

    fun sendMessage() {
        viewModelScope.launch {
            val message = "Ping"
            bluetoothController.trySendMessagePing(message)
        }
    }

    private fun Flow<ConnectionResult>.listen(): Job {
        return onEach {
            result ->
            when(result) {
                ConnectionResult.ConnectionEstablished -> {
                    _state.update {
                        it.copy(
                            isConnected = true,
                            isConnecting = false,
                            errorMessage = null,
                        )
                    }
                }
                is ConnectionResult.Error -> {
                    _state.update {
                        it.copy(
                            isConnected = false,
                            isConnecting = false,
                            errorMessage = result.message,
                        )
                    }
                }
                is ConnectionResult.TransferSucceeded -> {
                    Log.i("BLUETOOTHMAANGER", result.message)
                }
            }
        }.catch {
            throwable ->
            bluetoothController.closeConnection()
            _state.update {
                it.copy(
                    isConnected = false,
                    isConnecting = false,
                    errorMessage = null,
                )
            }
        }.launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        bluetoothController.release()
    }
}