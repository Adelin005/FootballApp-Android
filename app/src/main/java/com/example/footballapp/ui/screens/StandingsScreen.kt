package com.example.footballapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.res.stringResource
import com.example.footballapp.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.footballapp.FootballApp
import com.example.footballapp.ui.viewmodels.StandingsViewModel

@Composable
fun StandingsScreen(
    leagueId: String,
    leagueName: String,
    onBackClick: () -> Unit,
    viewModel: StandingsViewModel = viewModel()
) {
    LaunchedEffect(leagueId) { viewModel.loadStandings(leagueId) }

    val standings  by viewModel.standings.collectAsState()
    val isLoading  by viewModel.isLoading.collectAsState()
    val error      by viewModel.error.collectAsState()

    val favoritesManager = FootballApp.favoritesManager
    val favorites by favoritesManager.favorites.collectAsState()

    val settingsManager = FootballApp.settingsManager
    val isDarkMode by settingsManager.isDarkMode.collectAsState()

    val bgDark = if (isDarkMode) Color(0xFF0B132B) else Color(0xFFF9FAFB)
    val bgCard = if (isDarkMode) Color(0xFF1C2541) else Color.White
    val textColor = if (isDarkMode) Color.White else Color(0xFF1F2937)
    val subTextColor = if (isDarkMode) Color.Gray else Color.DarkGray
    val accent = Color(0xFF3B82F6)

    Surface(modifier = Modifier.fillMaxSize(), color = bgDark) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(bgCard)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = textColor
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = leagueName,
                        color = textColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.standings_subtitle, standings.size),
                        color = subTextColor,
                        fontSize = 13.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(bgCard.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⚽", fontSize = 14.sp)
                }
            }

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = accent)
                    }
                }

                error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = error ?: "", color = Color.Red)
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(standings) { index, team ->
                            val borderColor = when (index) {
                                0 -> Color(0xFFEAB308) // Gold
                                1 -> Color(0xFFD1D5DB) // Silver
                                2 -> Color(0xFFD97706) // Bronze
                                else -> Color.Gray.copy(alpha = 0.3f)
                            }
                            
                            val statusText = team.note ?: ""
                            val statusColor = when {
                                statusText.contains("Champions League", ignoreCase = true) -> Color(0xFF4ADE80) // Green
                                statusText.contains("Europa League", ignoreCase = true) -> Color(0xFF9CA3AF) // Grey
                                statusText.contains("Conference League", ignoreCase = true) -> Color(0xFF9CA3AF) // Grey
                                statusText.contains("Promotion", ignoreCase = true) -> Color(0xFF9CA3AF) // Grey
                                statusText.contains("Relegation", ignoreCase = true) -> Color(0xFFEF4444) // Red
                                else -> Color.Gray
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(bgCard)
                                    .padding(horizontal = 12.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .border(2.dp, borderColor, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        color = if (index < 3) borderColor else textColor,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                
                                Spacer(Modifier.width(12.dp))

                                // Shield Icon (Lighter Blue)
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF2563EB)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = Color(0xFF93C5FD), // Light blue shield
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Spacer(Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = team.teamName,
                                        color = textColor,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    if (statusText.isNotEmpty()) {
                                        Text(
                                            text = statusText,
                                            color = statusColor,
                                            fontSize = 10.sp,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            lineHeight = 12.sp
                                        )
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                                    StatItem("V", team.win.toString(), textColor)
                                    StatItem("E", team.draw.toString(), textColor)
                                    StatItem("Î", team.loss.toString(), textColor)
                                    StatItem("Pct", team.points.toString(), valColor = accent, textColor = textColor)
                                }

                                val isFav = favorites.any { it.teamName == team.teamName }
                                IconButton(onClick = { favoritesManager.toggleFavorite(leagueId, leagueName, team) }) {
                                    Icon(
                                        imageVector = if (isFav) Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = "Favorite",
                                        tint = if (isFav) Color(0xFFEAB308) else Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, textColor: Color, valColor: Color = textColor) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = Color.Gray, fontSize = 10.sp)
        Text(text = value, color = valColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}
