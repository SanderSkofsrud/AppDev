package com.example.oving_6_server

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
import com.example.oving_6_server.ui.theme.Oving_6_serverTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val hostViewModel by viewModels<HostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hostViewModel.startServer()
        setContent {
            Oving_6_serverTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ServerScreen(hostViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerScreen(hostViewModel: HostViewModel) {
    val messages by remember { derivedStateOf { hostViewModel.messages } }
    val messageState = remember { mutableStateOf(TextFieldValue()) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Server") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                items(messages) { message ->
                    Text(message, modifier = Modifier.padding(8.dp))
                    Divider()
                }
            }
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                TextField(
                    value = messageState.value,
                    onValueChange = { messageState.value = it },
                    label = { Text("Message to clients") },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        val text = messageState.value.text
                        if (text.isNotBlank()) {
                            scope.launch {
                                hostViewModel.broadcastMessage("Server: $text")
                                messageState.value = TextFieldValue()
                            }
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Send")
                }
            }
        }
    }
}
