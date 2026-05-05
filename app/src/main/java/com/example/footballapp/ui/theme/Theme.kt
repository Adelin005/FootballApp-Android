package com.example.footballapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF3B82F6),
    background = Color(0xFF0B132B), // Albastrul tău din Home
    surface = Color(0xFF111827),    // Culoare carduri
    onBackground = Color.White,
    onSurface = Color.White
)
private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    background = LightBackground,
    surface = LightCard,
    onBackground = LightText,
    onSurface = LightText
)

@Composable
fun FootballAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // îl lăsăm pe cel default momentan
        content = content
    )
}