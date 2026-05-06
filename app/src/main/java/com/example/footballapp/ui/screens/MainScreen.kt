package com.example.footballapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
    
    val hideBottomBar = currentRoute?.startsWith(Screen.Login.route) == true || 
                       currentRoute?.startsWith(Screen.SignUp.route) == true || 
                       currentRoute?.startsWith(Screen.Profile.route) == true

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
        Surface(color = bgColor) {
            NavHost(
                navController = navController,
                startDestination = startDest,
                modifier = Modifier.padding(innerPadding)
            ) {
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
                            navController.navigate(Screen.Profile.route) {
                                launchSingleTop = true
                            }
                        }
                    )
                }

                composable(Screen.Leagues.route) {
                    LeaguesScreen(onCountryClick = { code, name ->
                        val encodedCode = URLEncoder.encode(code, StandardCharsets.UTF_8.toString()).replace("+", "%20")
                        val encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString()).replace("+", "%20")
                        navController.navigate("country_leagues/$encodedCode/$encodedName")
                    })
                }

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
                            val encodedId = URLEncoder.encode(id, StandardCharsets.UTF_8.toString()).replace("+", "%20")
                            val encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString()).replace("+", "%20")
                            navController.navigate("teams/$encodedId/$encodedName")
                        }
                    )
                }

                composable(
                    "teams/{leagueId}/{leagueName}?highlightTeam={highlightTeam}",
                    arguments = listOf(
                        navArgument("leagueId") { type = NavType.StringType },
                        navArgument("leagueName") { type = NavType.StringType },
                        navArgument("highlightTeam") { 
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        }
                    )
                ) { backStackEntry ->
                    StandingsScreen(
                        leagueId = backStackEntry.arguments?.getString("leagueId") ?: "",
                        leagueName = backStackEntry.arguments?.getString("leagueName") ?: "",
                        highlightTeamName = backStackEntry.arguments?.getString("highlightTeam"),
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable(Screen.Favorites.route) {
                    FavoritesScreen(onTeamClick = { leagueId, leagueName, teamName ->
                        val encodedId = URLEncoder.encode(leagueId, StandardCharsets.UTF_8.toString()).replace("+", "%20")
                        val encodedName = URLEncoder.encode(leagueName, StandardCharsets.UTF_8.toString()).replace("+", "%20")
                        val encodedTeam = URLEncoder.encode(teamName, StandardCharsets.UTF_8.toString()).replace("+", "%20")
                        navController.navigate("teams/$encodedId/$encodedName?highlightTeam=$encodedTeam")
                    })
                }

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

                composable(Screen.Profile.route) {
                    ProfileInfoScreen(onBackClick = { navController.popBackStack() })
                }
            }
        }
    }
}
