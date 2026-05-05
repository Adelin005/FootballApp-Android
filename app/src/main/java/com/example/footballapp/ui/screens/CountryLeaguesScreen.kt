package com.example.footballapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.res.stringResource
import com.example.footballapp.R
import com.example.footballapp.ui.viewmodels.CountryLeaguesViewModel

@Composable
fun CountryLeaguesScreen(
    countryCode: String,
    countryName: String,
    onBackClick: () -> Unit,
    onLeagueClick: (String, String) -> Unit,
    viewModel: CountryLeaguesViewModel = viewModel()
) {
    LaunchedEffect(countryCode) { viewModel.loadLeagues(countryCode) }

    val leagues    by viewModel.leagues.collectAsState()
    val isLoading  by viewModel.isLoading.collectAsState()
    val error      by viewModel.error.collectAsState()

    val settingsManager = com.example.footballapp.FootballApp.settingsManager
    val isDarkMode by settingsManager.isDarkMode.collectAsState()

    val bgDark = if (isDarkMode) Color(0xFF0B132B) else Color(0xFFF9FAFB)
    val bgCard = if (isDarkMode) Color(0xFF1C2541) else Color.White
    val textColor = if (isDarkMode) Color.White else Color(0xFF1F2937)
    val subTextColor = if (isDarkMode) Color.Gray else Color.DarkGray
    val accent = Color(0xFF3B82F6)

    Surface(modifier = Modifier.fillMaxSize(), color = bgDark) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Header ──────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button
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

                // Titles
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = countryName,
                        color = textColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.country_leagues_subtitle),
                        color = subTextColor,
                        fontSize = 13.sp
                    )
                }

                // Blue Icon Right
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

            // ── Conținut ─────────────────────────────────────────────────────
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircularProgressIndicator(color = accent, strokeWidth = 3.dp)
                            Text(
                                stringResource(R.string.country_leagues_loading, countryName),
                                color = textColor.copy(alpha = 0.6f),
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Text("⚠️", fontSize = 40.sp)
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = error ?: stringResource(R.string.country_leagues_error),
                                color = Color(0xFFFF6B6B),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.loadLeagues(countryCode) },
                                colors = ButtonDefaults.buttonColors(containerColor = accent)
                            ) {
                                Text(stringResource(R.string.country_leagues_reload))
                            }
                        }
                    }
                }

                leagues.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            stringResource(R.string.country_leagues_empty, countryName),
                            color = subTextColor,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(32.dp)
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(leagues) { league ->
                            Card(
                                onClick = { onLeagueClick(league.id, league.name) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = bgCard),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Iconiță ligă
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(accent.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = Color(0xFFF59E0B),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }

                                    Spacer(Modifier.width(14.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = league.name,
                                            color = textColor,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 15.sp
                                        )
                                    }

                                    // Săgeată
                                    Text("›", color = accent, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}