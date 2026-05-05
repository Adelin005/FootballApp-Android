package com.example.footballapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.footballapp.ui.Screen
import com.example.footballapp.ui.bottomNavItems
import com.example.footballapp.ui.screens.StandingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    val settingsManager = com.example.footballapp.FootballApp.settingsManager
    val isDarkMode by settingsManager.isDarkMode.collectAsState()

    val bgColor = if (isDarkMode) Color(0xFF0B132B) else Color(0xFFF9FAFB)
    val bottomBarColor = Color(0xFF0B132B)
    val selectedItemColor = Color(0xFF3B82F6)
    val unselectedItemColor = Color.Gray
    
    val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
    val startDest = if (auth.currentUser != null) Screen.Home.route else Screen.Login.route
    
    val hideBottomBar = currentRoute in listOf(Screen.Login.route, Screen.SignUp.route, Screen.Profile.route)

    Scaffold(
        bottomBar = {
            if (!hideBottomBar) {
                NavigationBar(containerColor = bottomBarColor) {
                    bottomNavItems.forEach { screen ->
                        val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        NavigationBarItem(
                            icon = { screen.icon?.let { Icon(it, contentDescription = null) } },
                            label = { screen.titleResId?.let { Text(stringResource(it)) } },
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Această secțiune rezolvă problema revenirii la Home
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = selectedItemColor,
                            unselectedIconColor = unselectedItemColor,
                            indicatorColor = bottomBarColor
                        )
                    )
                }
            }
        }
    }) { innerPadding ->
        // Surface asigură că fundalul rămâne consistent între tranziții
        Surface(color = bgColor) {
            NavHost(
                navController = navController,
                startDestination = startDest,
                modifier = Modifier.padding(innerPadding)
            ) {
                // Auth
                composable(Screen.Login.route) {
                    LoginScreen(
                        onLoginSuccess = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        },
                        onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) }
                    )
                }
                
                composable(Screen.SignUp.route) {
                    SignUpScreen(
                        onSignUpSuccess = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        },
                        onNavigateToLogin = { navController.popBackStack() }
                    )
                }

                // Ecran 1: Home
                composable(Screen.Home.route) {
                    HomeScreen(
                        onExploreClick = { 
                            navController.navigate(Screen.Leagues.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onProfileClick = { 
                            navController.navigate(Screen.Settings.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }

                // Ecran 2: Lista Țări
                composable(Screen.Leagues.route) {
                    LeaguesScreen(onCountryClick = { code, name ->
                        navController.navigate("country_leagues/$code/$name")
                    })
                }

                // Ecran 3: Ligile dintr-o țară
                composable(
                    "country_leagues/{countryCode}/{countryName}",
                    arguments = listOf(
                        navArgument("countryCode") { type = NavType.StringType },
                        navArgument("countryName") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    CountryLeaguesScreen(
                        countryCode = backStackEntry.arguments?.getString("countryCode") ?: "",
                        countryName = backStackEntry.arguments?.getString("countryName") ?: "",
                        onBackClick = { navController.popBackStack() },
                        onLeagueClick = { id, name ->
                            navController.navigate("teams/$id/$name")
                        }
                    )
                }

// Ecran 4: Clasament ligă (REPARAT - acum afișează standings cu puncte)
                composable(
                    "teams/{leagueId}/{leagueName}",
                    arguments = listOf(
                        navArgument("leagueId") { type = NavType.StringType },
                        navArgument("leagueName") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    StandingsScreen(
                        leagueId = backStackEntry.arguments?.getString("leagueId") ?: "",
                        leagueName = backStackEntry.arguments?.getString("leagueName") ?: "",
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // Ecran 5: Favorite
                composable(Screen.Favorites.route) {
                    FavoritesScreen()
                }

                // Ecran 6: Setări
                composable(Screen.Settings.route) {
                    SettingsScreen(
                        onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                        onBackClick = { 
                            navController.navigate(Screen.Home.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onLogout = {
                            auth.signOut()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }

                // Ecran 7: Profil Informații
                composable(Screen.Profile.route) {
                    ProfileInfoScreen(onBackClick = { navController.popBackStack() })
                }
            }
        }
    }
}