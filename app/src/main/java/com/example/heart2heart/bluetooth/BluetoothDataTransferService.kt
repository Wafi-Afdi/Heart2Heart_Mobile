package com.example.heart2heart.bluetooth

import android.bluetooth.BluetoothSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException

class BluetoothDataTransferService(
    private val socket: BluetoothSocket
) {
    fun listenForIncomingMessage(): Flow<String> {
        return flow {
            if (!socket.isConnected) {
                return@flow
            }
            val buffer = ByteArray(1024)
            while (true) {
                val byteCount = try {
                    socket.inputStream.read(buffer)
                } catch (e: IOException) {
                    throw TransferFailedException()
                }
                emit(buffer.decodeToString(
                    endIndex = byteCount,
                ))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun sendPing(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                socket.outputStream.write(byteArrayOf('P'.code.toByte()))
            } catch (e: IOException) {
                return@withContext false
            }

            true
        }
    }
}