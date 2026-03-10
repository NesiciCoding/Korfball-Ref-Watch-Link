package com.wmeetsma.korfballrefwatch.network

import android.util.Log
import com.wmeetsma.korfballrefwatch.repository.GameStateRepository
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject

object SocketManager {
    private var socket: Socket? = null

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    enum class ConnectionState { Disconnected, Connecting, Connected, Error }

    fun connect(ipAddress: String, port: Int = 3002) {
        _connectionState.value = ConnectionState.Connecting
        try {
            socket?.disconnect()
            socket = null

            val opts = IO.Options.builder()
                .setReconnection(true)
                .setReconnectionAttempts(Int.MAX_VALUE)
                .setReconnectionDelay(2000)
                .build()

            val url = "http://$ipAddress:$port"
            Log.d("SocketManager", "Connecting to $url")
            socket = IO.socket(url, opts)

            socket?.on(Socket.EVENT_CONNECT) {
                Log.d("SocketManager", "✅ Connected to $url")
                _connectionState.value = ConnectionState.Connected
            }

            socket?.on("watch-update") { args ->
                if (args.isNotEmpty()) {
                    try {
                        val dataJson = args[0] as JSONObject
                        val dataMap = mutableMapOf<String, Any>()
                        val keys = dataJson.keys()
                        while (keys.hasNext()) {
                            val key = keys.next() as String
                            dataMap[key] = dataJson.get(key)
                        }
                        GameStateRepository.updateFromMap(dataMap)
                    } catch (e: Exception) {
                        Log.e("SocketManager", "Error parsing watch-update", e)
                    }
                }
            }

            socket?.on("haptic-signal") { args ->
                if (args.isNotEmpty()) {
                    try {
                        val dataJson = args[0] as JSONObject
                        val type = dataJson.optString("hapticSignal", null)
                        val id = dataJson.optString("hapticSignalId", null)
                        if (type != null && id != null) {
                            GameStateRepository.updateFromMap(
                                mapOf("hapticSignal" to type, "hapticSignalId" to id)
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("SocketManager", "Error parsing haptic-signal", e)
                    }
                }
            }

            socket?.on(Socket.EVENT_DISCONNECT) {
                Log.d("SocketManager", "Disconnected from server")
                _connectionState.value = ConnectionState.Disconnected
            }

            socket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
                Log.e("SocketManager", "Connection error: ${args.firstOrNull()}")
                _connectionState.value = ConnectionState.Error
            }

            socket?.connect()
        } catch (e: Exception) {
            Log.e("SocketManager", "Socket setup error", e)
            _connectionState.value = ConnectionState.Error
        }
    }

    /**
     * Emit a watch-action event back to the server so the web app can apply it.
     * action: one of "TOGGLE_GAME_TIME", "RESET_SHOT_CLOCK"
     */
    fun emitAction(action: String) {
        if (_connectionState.value != ConnectionState.Connected) return
        try {
            val payload = JSONObject()
            payload.put("action", action)
            socket?.emit("watch-action", payload)
            Log.d("SocketManager", "Emitted watch-action: $action")
        } catch (e: Exception) {
            Log.e("SocketManager", "Error emitting watch-action", e)
        }
    }

    fun disconnect() {
        socket?.disconnect()
        socket = null
        _connectionState.value = ConnectionState.Disconnected
    }
}
