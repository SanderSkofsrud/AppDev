package com.example.oving_7

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    databaseHelper: DatabaseHelper,
    navController: NavController,
    selectedColor: MutableState<Color>
) {
    var title by remember { mutableStateOf("Velkommen") }
    var results by remember { mutableStateOf(listOf<String>()) }

    var showDirectorDialog by remember { mutableStateOf(false) }
    var showMovieDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filmapp") },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("settings")
                    }) {
                        Icon(Icons.Default.Settings, contentDescription = "Innstillinger")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(selectedColor.value)
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(title, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                if (results.isNotEmpty()) {
                    results.forEach { result ->
                        Text(result)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    title = "Alle filmer"
                    results = databaseHelper.getAllMovies()
                }) {
                    Text("Alle filmer")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    showDirectorDialog = true
                }) {
                    Text("Filmer av regissør")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    showMovieDialog = true
                }) {
                    Text("Skuespillere i film")
                }
            }

            if (showDirectorDialog) {
                InputDialog(
                    title = "Skriv inn regissørens navn",
                    onDismiss = { showDirectorDialog = false },
                    onConfirm = { director: String ->
                        title = "Filmer av $director"
                        results = databaseHelper.getMoviesByDirector(director)
                        showDirectorDialog = false
                    }
                )
            }

            if (showMovieDialog) {
                InputDialog(
                    title = "Skriv inn filmens tittel",
                    onDismiss = { showMovieDialog = false },
                    onConfirm = { movieTitle: String ->
                        title = "Skuespillere i $movieTitle"
                        results = databaseHelper.getActorsByMovie(movieTitle)
                        showMovieDialog = false
                    }
                )
            }
        }
    )
}
