package com.example.footballapp.ui.screens

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.footballapp.R
import com.example.footballapp.FootballApp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SettingsScreen(
    onNavigateToProfile: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val settingsManager = FootballApp.settingsManager
    
    val isDarkMode by settingsManager.isDarkMode.collectAsState()
    val auth = FirebaseAuth.getInstance()
    
    val bgDark = if (isDarkMode) Color(0xFF0F1528) else Color(0xFFF9FAFB)
    val cardBg = if (isDarkMode) Color(0xFF161E38) else Color.White
    val textColor = if (isDarkMode) Color.White else Color(0xFF1F2937)
    val sectionTitleColor = if (isDarkMode) Color(0xFF8C95AD) else Color.Gray
    val accentBlue = Color(0xFF3B82F6)

    Surface(modifier = Modifier.fillMaxSize(), color = bgDark) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Înapoi",
                        tint = textColor
                    )
                }
                Text(
                    text = stringResource(R.string.settings_title), 
                    color = textColor, 
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(48.dp)) // Spacer to balance the back button
            }
            
            Spacer(modifier = Modifier.height(30.dp))

            // Profil Card "Salut Ade"
            // Extragem numele sau preluăm o parte din email dacă nu există displayName
            val userName = auth.currentUser?.displayName?.takeIf { it.isNotBlank() }
                ?: auth.currentUser?.email?.substringBefore("@") ?: "Utilizator"

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBg)
                    .padding(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.settings_greeting, userName),
                    color = textColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Secțiune CONT
            Text(
                text = stringResource(R.string.settings_account),
                color = sectionTitleColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBg)
                    .clickable { onNavigateToProfile() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = accentBlue)
                Spacer(modifier = Modifier.width(16.dp))
                Text(stringResource(R.string.settings_profile_info), color = textColor, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Secțiune PREFERINȚE
            Text(
                text = stringResource(R.string.settings_preferences),
                color = sectionTitleColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBg)
            ) {
                // Mod Întunecat
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.DarkMode, contentDescription = null, tint = accentBlue)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(stringResource(R.string.settings_dark_mode), color = textColor, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                    Switch(
                        checked = isDarkMode, 
                        onCheckedChange = { settingsManager.setDarkMode(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = accentBlue,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.LightGray
                        )
                    )
                }

                Divider(color = Color(0xFF283252), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))

                // Limbă
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { context.startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS)) }
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Translate, contentDescription = null, tint = accentBlue)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(stringResource(R.string.settings_language), color = textColor, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                    
                    val currentLang = java.util.Locale.getDefault().displayLanguage
                    val displayLang = currentLang.replaceFirstChar { 
                        if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() 
                    }
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(displayLang, color = accentBlue, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = accentBlue, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Buton Deconectare
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B161B)) // Background roșiatic întunecat
            ) {
                Text(stringResource(R.string.settings_logout), color = Color(0xFFEF4444), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Versiune App
            Text(
                text = "Versiunea 4.12.0 (Build 829)",
                color = sectionTitleColor,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}