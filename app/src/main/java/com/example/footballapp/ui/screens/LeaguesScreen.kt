package com.example.footballapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.ui.res.stringResource
import com.example.footballapp.R
import com.example.footballapp.ui.viewmodels.LeaguesViewModel

// IMPORTURI CRITICE PENTRU ECHILIBRUL COLECTĂRII DE DATE
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun LeaguesScreen(
    onCountryClick: (String, String) -> Unit,
    viewModel: LeaguesViewModel = viewModel()
) {
    // Colectăm datele din ViewModel
    val countries by viewModel.countries.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // State local pentru căutare
    var searchQuery by remember { mutableStateOf("") }

    val filteredCountries = countries.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    val settingsManager = com.example.footballapp.FootballApp.settingsManager
    val isDarkMode by settingsManager.isDarkMode.collectAsState()

    val bgDark = if (isDarkMode) Color(0xFF0B132B) else Color(0xFFF9FAFB)
    val bgCard = if (isDarkMode) Color(0xFF1C2541) else Color.White
    val textColor = if (isDarkMode) Color.White else Color(0xFF1F2937)

    Surface(modifier = Modifier.fillMaxSize(), color = bgDark) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Public,
                    contentDescription = null,
                    tint = Color(0xFF3B82F6),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.leagues_title),
                    color = textColor,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.leagues_subtitle),
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bara de căutare
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = { Text(stringResource(R.string.leagues_search_hint), color = Color.Gray) },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = bgCard,
                    unfocusedContainerColor = bgCard,
                    disabledContainerColor = bgCard,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = textColor,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF3B82F6))
                }
            } else {
                LazyColumn {
                    items(filteredCountries) { country ->
                        CountryItem(
                            name = country.name,
                            code = country.code ?: "",
                            isDarkMode = isDarkMode,
                            onClick = { onCountryClick(country.code ?: "", country.name) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CountryItem(name: String, code: String, isDarkMode: Boolean, onClick: () -> Unit) {
    val cardBg = if (isDarkMode) Color(0xFF1C2541) else Color.White
    val textColor = if (isDarkMode) Color.White else Color(0xFF1F2937)
    val subTextColor = if (isDarkMode) Color.Gray else Color.DarkGray

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flag Image folosind Coil
            if (code.isNotBlank() && code.lowercase() != "world") {
                AsyncImage(
                    model = "https://flagcdn.com/w80/${code.lowercase()}.png",
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF2A3A54))
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}