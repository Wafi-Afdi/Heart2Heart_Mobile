package com.example.heart2heart.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.State
import com.example.heart2heart.ECGExtraction.domain.TextUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDateTime
import java.util.UUID


@SuppressLint("MissingPermission")
class BluetoothServiceECG(
    private val context: Context
): BluetoothController
{
    private var isFoundDeviceReceiverRegistered = false
    private var isBluetoothStateReceiverRegistered = false

    private val _lastConnectedTime = MutableStateFlow<LocalDateTime?>(null)
    val lastConnectedTime: StateFlow<LocalDateTime?>
        get() = _lastConnectedTime.asStateFlow()
    private var currentServerSocket: BluetoothServerSocket? = null
    private var currentClientSocket: BluetoothSocket? = null

    private var dataTransferService: BluetoothDataTransferService? = null

    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val _scannedDevices = MutableStateFlow<List<BluetoothDataModel>>(emptyList())

    override val scannedDevices: StateFlow<List<BluetoothDataModel>>
        get() = _scannedDevices.asStateFlow()

    private val _pairedDevices = MutableStateFlow<List<BluetoothDataModel>>(emptyList())
    override val pairedDevices: StateFlow<List<BluetoothDataModel>>
        get() = _pairedDevices.asStateFlow()

    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean>
        get() = _isConnected.asStateFlow()

    private val _errors = MutableSharedFlow<String>()
    override val errors: SharedFlow<String>
        get() = _errors.asSharedFlow()

//    private val serviceJob = SupervisorJob()
//    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    private val _incomingSamples = MutableSharedFlow<String>(replay = 100, extraBufferCapacity = 1000)
    val incomingSamples: SharedFlow<String> = _incomingSamples.asSharedFlow()


    private val bluetoothStateReceiver = BluetoothStateReceiver { isConnected, device ->
        if (bluetoothAdapter?.bondedDevices?.contains(device) == true) {
            _isConnected.update { isConnected }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                _errors.tryEmit("Can't connect to device")
            }
        }
    }


    init {
        try {
            context.registerReceiver(
                bluetoothStateReceiver,
                IntentFilter().apply {
                    addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
                    addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
                    addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
                }
            )
            isBluetoothStateReceiverRegistered = true // <-- SET FLAG
        } catch (e: Exception) {
            isBluetoothStateReceiverRegistered = false
            Log.e("BluetoothServiceECG", "Failed to register bluetoothStateReceiver", e)
            CoroutineScope(Dispatchers.IO).launch {
                _errors.emit("Failed to initialize Bluetooth state receiver.")
            }
        }
    }

    private val foundDeviceReceiver = FoundDeviceReceiver { device ->
        _scannedDevices.update { devices ->
            val newDevice = device.toBluetoothDeviceDomain()
            if(newDevice in devices) devices else devices + newDevice
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN])
    override fun startDiscovery() {
        if(!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            return
        }

        if (isFoundDeviceReceiverRegistered) {
            Log.w("BluetoothServiceECG", "FoundDeviceReceiver already registered.")
            return
        }

        try {
            context.registerReceiver(
                foundDeviceReceiver,
                IntentFilter(BluetoothDevice.ACTION_FOUND)
            )
            isFoundDeviceReceiverRegistered = true // <-- SET FLAG
        } catch (e: Exception) {
            isFoundDeviceReceiverRegistered = false
            Log.e("BluetoothServiceECG", "Failed to register foundDeviceReceiver", e)
            CoroutineScope(Dispatchers.IO).launch {
                _errors.emit("Failed to start device discovery.")
            }
            return
        }
        updatePairedDevices()

        bluetoothAdapter?.startDiscovery()
    }

    override fun stopDiscovery() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            return
        }

        bluetoothAdapter?.cancelDiscovery()

        if (isFoundDeviceReceiverRegistered) {
            try {
                context.unregisterReceiver(foundDeviceReceiver)
                isFoundDeviceReceiverRegistered = false // <-- CLEAR FLAG
            } catch (e: IllegalArgumentException) {
                Log.w("BluetoothServiceECG", "stopDiscovery: foundDeviceReceiver was already unregistered.")
            }
        }
    }

    override fun startBluetoothServer(): Flow<ConnectionResult> {
        return flow {
            if(!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
                throw SecurityException("No BLUETOOTH_CONNECT permission")
            }
            currentServerSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord(
                "Heart2Heart",
                UUID.fromString(SERVICE_UUID)
            )

            var shouldLoop = true
            while(shouldLoop) {
                currentClientSocket = try {
                    currentServerSocket?.accept()
                } catch (e: IOException) {
                    shouldLoop = false
                    null
                }
                emit(ConnectionResult.ConnectionEstablished)
                currentClientSocket?.let { it ->
                    currentServerSocket?.close()
                    val service = BluetoothDataTransferService(it)
                    dataTransferService = service

                    emitAll(service.listenForIncomingMessage().map {
                        ConnectionResult.TransferSucceeded(it.toString())
                    })
                }
            }
        }.onCompletion {
            closeConnection()
        }.flowOn(Dispatchers.IO)
    }


    private var _pendingCarriageReturn = false
    private val _buffer = StringBuilder()

    override fun connectToDevice(device: BluetoothDataModel): Flow<ConnectionResult> {
        return flow {
            if(!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
                throw SecurityException("No BLUETOOTH_CONNECT permission")
            }
            Log.i("BLUETOOTHECG", "Device ID ${device.address}")

            val bluetoothDevice = bluetoothAdapter?.getRemoteDevice(device.address)


            currentClientSocket = bluetoothDevice
                ?.createRfcommSocketToServiceRecord(
                    UUID.fromString(SERVICE_UUID)
                )
            stopDiscovery()

            currentClientSocket?.let {
                socket ->
                try {
                    Log.i("BLUETOOTHECG", "Connection Trying")
                    socket.connect()
                    emit(ConnectionResult.ConnectionEstablished)
                    Log.i("BLUETOOTHECG", "Connection Established")
                    _lastConnectedTime.update { LocalDateTime.now() }
                    BluetoothDataTransferService(socket).also {
                        dataTransferService = it
                        it
                                .listenForIncomingMessage()
                                .collect { raw ->
                                    val textChunk = raw.decodeToString()
                                    _buffer.append(textChunk)

                                    var delimiterIndex: Int
                                    while (true) {
                                        delimiterIndex = _buffer.indexOf("\r\n")
                                        if (delimiterIndex == -1) break // No full message yet

                                        // Extract one full line
                                        val message = _buffer.substring(0, delimiterIndex)
                                        _buffer.delete(0, delimiterIndex + 2) // Remove up to CRLF

                                        if (message.isNotBlank()) {
                                            // Log.w("BluetoothServiceECG", message)
                                            _incomingSamples.emit(message)
                                        }
                                    }
//                                val samples = parser.feed(raw)
//                                samples.forEach { sample ->
//                                    // emit into your shared flow, or process/store
//                                    _incomingSamples.emit(raw)
//                                }
//                                val sample = raw.toDoubleOrNull()
//                                if (sample != null) {
//                                    _incomingSamples.emit(sample)
//                                } else {
//                                    Log.w("BluetoothServiceECG", "Non-numeric data: $raw")
//                                }
                        }
//                        emitAll(
//                            it.listenForIncomingMessage()
//                                .catch {
//                                    emit(ConnectionResult.Error("Connection was interrupted"))
//                                }
//                                .map {
//                                ConnectionResult.TransferSucceeded(it.toString())
//                            }
//                        )
                    }

                } catch (e: IOException) {
                    Log.e("BLUETOOTHECG", "Connection Failed")
                    socket.close()
                    currentClientSocket = null
                    emit(ConnectionResult.Error("Connection was interrupted"))
                }
            }
        }.catch {
            emit(ConnectionResult.Error("Connection was interrupted"))
            closeConnection()
        }.onCompletion {
            closeConnection()
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun trySendMessagePing(message: String) {
        if(!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            println("Bluetooth Not Active")
            return
        }

        if (dataTransferService == null) {
            println("Bluetooth Service not active")
        }

        dataTransferService?.sendPing()
    }

    override fun closeConnection() {
        currentClientSocket?.close()
        currentServerSocket?.close()
        currentClientSocket = null
        currentServerSocket = null
        _lastConnectedTime.update { null }
    }

    override fun release() {
        stopDiscovery()
        if (isBluetoothStateReceiverRegistered) {
            try {
                context.unregisterReceiver(bluetoothStateReceiver)
                isBluetoothStateReceiverRegistered = false
            } catch (e: IllegalArgumentException) {
                Log.w("BluetoothServiceECG", "release: bluetoothStateReceiver already unregistered.")
            }
        }

        // This check is a final safety net in case stopDiscovery() failed
        if (isFoundDeviceReceiverRegistered) {
            try {
                context.unregisterReceiver(foundDeviceReceiver)
                isFoundDeviceReceiverRegistered = false
            } catch (e: IllegalArgumentException) {
                Log.w("BluetoothServiceECG", "release: foundDeviceReceiver already unregistered.")
            }
        }
        closeConnection()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun updatePairedDevices() {
        if(!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            return
        }
        bluetoothAdapter
            ?.bondedDevices
            ?.map {
                it.toBluetoothDeviceDomain()
            }
            ?.also {
                devices ->
                _pairedDevices.update { devices }
            }
    }

    private fun hasPermission(permission: String): Boolean {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        const val SERVICE_UUID = "00001101-0000-1000-8000-00805F9B34FB"
    }
}