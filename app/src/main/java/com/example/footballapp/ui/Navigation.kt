package com.example.footballapp.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.footballapp.R

sealed class Screen(val route: String, val titleResId: Int? = null, val icon: ImageVector? = null) {
    object Home : Screen("home", R.string.tab_home, Icons.Default.Home)
    object Leagues : Screen("leagues", R.string.tab_leagues, Icons.Default.EmojiEvents)
    object Favorites : Screen("favorites", R.string.tab_favorites, Icons.Default.Star)
    object Settings : Screen("settings", R.string.tab_settings, Icons.Default.Settings)

    // Rută pentru detalii țară (fără iconiță în meniul de jos)
    object CountryLeagues : Screen("country_leagues/{countryCode}/{countryName}") {
        fun createRoute(code: String, name: String) = "country_leagues/$code/$name"
    }
    
    // Auth
    object Login : Screen("login")
    object SignUp : Screen("signup")
    // Profile
    object Profile : Screen("profile_info")
    
    // Standings
    object Teams : Screen("teams/{leagueId}/{leagueName}") {
        fun createRoute(id: String, name: String) = "teams/$id/$name"
    }
}

val bottomNavItems = listOf(Screen.Home, Screen.Leagues, Screen.Favorites, Screen.Settings)