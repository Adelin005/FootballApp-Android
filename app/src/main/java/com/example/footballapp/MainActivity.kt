package com.example.footballapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.footballapp.ui.theme.FootballAppTheme
import com.example.footballapp.ui.screens.HomeScreen // IMPORTUL CRUCIAL
import com.example.footballapp.ui.screens.MainScreen // Îl vom folosi imediat ce îl creezi

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())

        setContent {
            FootballAppTheme {
                // ÎNLOCUIEȘTE HomeScreen cu MainScreen
                MainScreen()
            }
        }
    }
}