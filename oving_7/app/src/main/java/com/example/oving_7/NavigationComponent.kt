package com.example.oving_7

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationComponent(databaseHelper: DatabaseHelper, selectedColor: MutableState<Color>) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(databaseHelper, navController, selectedColor)
        }
        composable("settings") {
            SettingsScreen(navController, selectedColor)
        }
    }
}
