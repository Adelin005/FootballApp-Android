package com.example.footballapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.outlined.ShareLocation
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.example.footballapp.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.footballapp.FootballApp
import com.example.footballapp.data.FavoriteTeam

@Composable
fun FavoritesScreen() {
    val favoritesManager = FootballApp.favoritesManager
    val settingsManager = FootballApp.settingsManager
    val favorites by favoritesManager.favorites.collectAsState()

    val isDarkMode by settingsManager.isDarkMode.collectAsState()

    val bgDark = if (isDarkMode) Color(0xFF0B132B) else Color(0xFFF9FAFB)
    val cardBg = if (isDarkMode) Color(0xFF1C2541) else Color.White
    val textColor = if (isDarkMode) Color.White else Color(0xFF1F2937)
    val accent = Color(0xFFEAB308) // Portocaliu/Auriu pentru stea

    Surface(modifier = Modifier.fillMaxSize(), color = bgDark) {
        Column(modifier = Modifier.fillMaxSize()) {
            
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Star, contentDescription = null, tint = accent, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.fav_title),
                    color = textColor,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                
                // Numar echipe
                if (favorites.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(accent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(favorites.size.toString(), color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Text(
                text = stringResource(R.string.fav_desc),
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Banner Offline
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Color(0xFF0F3D1F), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CloudDone, contentDescription = null, tint = Color(0xFF4ADE80), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.fav_offline_banner), color = Color(0xFF4ADE80), fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (favorites.isEmpty()) {
                // Empty state
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.StarBorder,
                        contentDescription = null,
                        tint = Color(0xFF1E293B),
                        modifier = Modifier.size(100.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = stringResource(R.string.fav_empty_title),
                        color = textColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.fav_empty_desc),
                        color = Color.Gray,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    // Breadcrumb tip
                    Row(
                        modifier = Modifier
                            .background(cardBg, RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.ShareLocation, contentDescription = null, tint = Color(0xFF3B82F6), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.fav_breadcrumb),
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Icon(Icons.Default.Star, contentDescription = null, tint = accent, modifier = Modifier.size(14.dp))
                        Text(
                            text = stringResource(R.string.fav_breadcrumb_team),
                            color = Color.LightGray,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                // Populated list
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(favorites) { favorite ->
                        FavoriteTeamCard(favorite, isDarkMode, onRemove = {
                            favoritesManager.toggleFavorite(favorite.leagueName, favorite.leagueName, favorite.teamStanding)
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteTeamCard(favorite: FavoriteTeam, isDarkMode: Boolean = true, onRemove: () -> Unit) {
    val cardBg = if (isDarkMode) Color(0xFF1C2541) else Color.White
    val textColor = if (isDarkMode) Color.White else Color(0xFF1F2937)
    val subTextColor = if (isDarkMode) Color.LightGray else Color.Gray
    val badgeBg = if (isDarkMode) Color(0xFF334155) else Color(0xFFE2E8F0)
    val badgeTextColor = if (isDarkMode) Color.LightGray else Color(0xFF334155)
    val accentStar = Color(0xFFEAB308)
    
    val team = favorite.teamStanding

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder Shield
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF2A3A54)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFF3B82F6),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = team.teamName,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = favorite.leagueName, color = subTextColor, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Stats row
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Position
                    StatBadge("#${team.position}", badgeBg, badgeTextColor)
                    // Points
                    StatBadge("${team.points} pts", badgeBg, badgeTextColor)
                    // Form
                    StatBadge("${team.win}V ${team.draw}E ${team.loss}Î", badgeBg, badgeTextColor)
                    
                    val goalDiff = team.goalsFor - team.goalsAgainst
                    val diffColor = if (goalDiff > 0) Color(0xFF22C55E) else if (goalDiff < 0) Color(0xFFEF4444) else Color.Gray
                    StatBadge(if (goalDiff > 0) "+$goalDiff" else "$goalDiff", diffColor.copy(alpha = 0.2f), diffColor)
                }
                
                if (!team.note.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = team.note,
                        color = Color(0xFF22C55E),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Star
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Remove Favorite",
                    tint = accentStar,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun StatBadge(text: String, bgColor: Color, textColor: Color = Color.LightGray) {
    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text, color = textColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}