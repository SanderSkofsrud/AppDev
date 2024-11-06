package com.example.oving_7

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, selectedColor: MutableState<Color>) {
    val colorOptions = listOf(
        Color.White to "Hvit",
        Color.LightGray to "Lys Grå",
        Color.Yellow to "Gul"
    )

    var selectedOption by remember {
        mutableStateOf(colorOptions.find { it.first == selectedColor.value } ?: colorOptions[0])
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Innstillinger") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Tilbake")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text("Velg bakgrunnsfarge", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                colorOptions.forEach { (colorValue, colorName) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = selectedOption == colorValue to colorName,
                            onClick = {
                                selectedOption = colorValue to colorName
                                selectedColor.value = colorValue
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(colorName)
                    }
                }
            }
        }
    )
}
