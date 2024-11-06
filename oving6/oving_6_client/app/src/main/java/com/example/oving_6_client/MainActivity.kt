package com.example.oving_6_client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.oving_6_client.ui.theme.Oving_6_clientTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val clientViewModel by viewModels<ClientViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clientViewModel.connectToServer()
        setContent {
            Oving_6_clientTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ClientScreen(clientViewModel)
                }
            }
        }
    }
}

@Composable
fun ClientScreen(clientViewModel: ClientViewModel) {
    val messages = clientViewModel.messages
    val isConnected by clientViewModel.isConnected
    val messageState = remember { mutableStateOf(TextFieldValue()) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(messages) { message ->
                Text(message, modifier = Modifier.padding(4.dp))
                Divider()
            }
        }
        if (isConnected) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = messageState.value,
                    onValueChange = { messageState.value = it },
                    label = { Text("Message") },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        val text = messageState.value.text
                        if (text.isNotBlank()) {
                            scope.launch {
                                clientViewModel.sendMessage(text)
                                messageState.value = TextFieldValue()
                            }
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Send")
                }
            }
        } else {
            Text("Connecting...", modifier = Modifier.padding(8.dp))
        }
    }
}
