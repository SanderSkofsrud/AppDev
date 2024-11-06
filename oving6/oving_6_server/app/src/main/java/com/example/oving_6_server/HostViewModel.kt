package com.example.oving_6_server

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.net.ServerSocket
import java.net.Socket

class HostViewModel : ViewModel() {

    private val SERVER_PORT = 6000
    private var serverSocket: ServerSocket? = null
    private val clientHandlers = mutableListOf<ClientHandler>()

    var messages = mutableStateListOf<String>()
        private set

    fun startServer() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                serverSocket = ServerSocket(SERVER_PORT)
                addMessage("Server started on port $SERVER_PORT")

                while (true) {
                    val clientSocket = serverSocket!!.accept()
                    val clientInfo = clientSocket.inetAddress.hostAddress
                    addMessage("Client connected: $clientInfo")

                    val clientHandler = ClientHandler(clientSocket)
                    clientHandlers.add(clientHandler)
                    clientHandler.start()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                addMessage("Server error: ${e.message}")
            }
        }
    }

    inner class ClientHandler(private val socket: Socket) {

        private val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        private val writer = PrintWriter(socket.getOutputStream(), true)

        fun start() {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    sendMessage("Welcome to the server!")
                    var message: String?
                    while (socket.isConnected) {
                        message = reader.readLine()
                        if (message != null) {
                            addMessage("Received: $message")
                            // Broadcast to other clients, excluding the sender
                            sendMessageToClients("Client says: $message", this@ClientHandler)
                        } else {
                            break
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    addMessage("Client error: ${e.message}")
                } finally {
                    socket.close()
                    clientHandlers.remove(this@ClientHandler)
                    addMessage("Client disconnected")
                }
            }
        }

        fun sendMessage(message: String) {
            writer.println(message)
        }
    }

    // Make sendMessageToClients public
    fun sendMessageToClients(message: String, sender: ClientHandler) {
        viewModelScope.launch(Dispatchers.IO) {
            clientHandlers.filter { it != sender }.forEach { client ->
                try {
                    client.sendMessage(message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            addMessage("Sent to clients: $message")
        }
    }

    // Additional function to broadcast to all clients
    fun broadcastMessage(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            clientHandlers.forEach { client ->
                try {
                    client.sendMessage(message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            addMessage("Sent to clients: $message")
        }
    }

    private suspend fun addMessage(message: String) {
        withContext(Dispatchers.Main) {
            messages.add(message)
        }
    }
}
