package com.example.oving_5

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val client = OkHttpClient.Builder()
        .cookieJar(object : CookieJar {
            private var cookies: List<Cookie> = mutableListOf()
            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                return cookies
            }

            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                this.cookies = cookies
            }
        })
        .build()

    private val baseUrl = "https://bigdata.idi.ntnu.no/mobil/tallspill.jsp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuessingGameApp()
        }
    }

    @Composable
    fun GuessingGameApp() {
        var name by remember { mutableStateOf("") }
        var cardNumber by remember { mutableStateOf("") }
        var guess by remember { mutableStateOf("") }
        var message by remember { mutableStateOf("") }
        var showGuessInput by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Guessing Game") }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Enter your name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { cardNumber = it },
                    label = { Text("Enter your card number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        startGame(name, cardNumber) { response, shouldShowGuess ->
                            message = response
                            showGuessInput = shouldShowGuess
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Start Game")
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (showGuessInput) {
                    OutlinedTextField(
                        value = guess,
                        onValueChange = { guess = it },
                        label = { Text("Enter your guess") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            submitGuess(guess) { response ->
                                message = response
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Submit Guess")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Text(text = message)
            }
        }
    }

    private fun startGame(
        name: String,
        cardNumber: String,
        callback: (String, Boolean) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val formBody = FormBody.Builder()
                .add("navn", name)
                .add("kortnummer", cardNumber)
                .build()

            val request = Request.Builder()
                .url(baseUrl)
                .post(formBody)
                .build()

            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                Log.d("Response", "StartGame Response: $responseBody")

                val shouldShowGuess = responseBody?.contains("Oppgi et tall mellom") == true
                withContext(Dispatchers.Main) {
                    callback(responseBody ?: "No response", shouldShowGuess)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    callback("An error occurred", false)
                }
            }
        }
    }

    private fun submitGuess(
        guess: String,
        callback: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val formBody = FormBody.Builder()
                .add("tall", guess)
                .build()

            val request = Request.Builder()
                .url(baseUrl)
                .post(formBody)
                .build()

            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                Log.d("Response", "SubmitGuess Response: $responseBody")

                withContext(Dispatchers.Main) {
                    callback(responseBody ?: "No response")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    callback("An error occurred")
                }
            }
        }
    }
}
