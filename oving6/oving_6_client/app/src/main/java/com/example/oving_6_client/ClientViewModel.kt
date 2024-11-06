package com.example.oving_6_client

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.io.*
import java.net.Socket

class ClientViewModel : ViewModel() {

    private val SERVER_IP: String = "10.0.2.2"
    private val SERVER_PORT: Int = 6000

    private var socket: Socket? = null
    private var reader: BufferedReader? = null
    private var writer: PrintWriter? = null

    var messages = mutableStateListOf<String>()
        private set

    var isConnected = mutableStateOf(false)
        private set

    fun connectToServer() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                socket = Socket(SERVER_IP, SERVER_PORT)
                writer = PrintWriter(socket!!.getOutputStream(), true)
                reader = BufferedReader(InputStreamReader(socket!!.getInputStream()))

                isConnected.value = true
                addMessage("Connected to server")

                // Start listening for messages
                listenForMessages()
            } catch (e: Exception) {
                e.printStackTrace()
                addMessage("Connection error: ${e.message}")
            }
        }
    }

    private fun listenForMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var message: String?
                while (socket!!.isConnected) {
                    message = reader?.readLine()
                    if (message != null) {
                        addMessage("Received: $message")
                    } else {
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                addMessage("Connection lost: ${e.message}")
            } finally {
                socket?.close()
                isConnected.value = false
                addMessage("Disconnected from server")
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                writer?.println(message)
                addMessage("Sent: $message")
            } catch (e: Exception) {
                e.printStackTrace()
                addMessage("Send error: ${e.message}")
            }
        }
    }

    private suspend fun addMessage(message: String) {
        withContext(Dispatchers.Main) {
            messages.add(message)
        }
    }
}
