package com.example.footballapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.footballapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInfoScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    // State for inputs
    var displayName by remember { mutableStateOf(user?.displayName ?: "") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var isSavingProfile by remember { mutableStateOf(false) }
    var isChangingPassword by remember { mutableStateOf(false) }

    val settingsManager = com.example.footballapp.FootballApp.settingsManager
    val isDarkMode by settingsManager.isDarkMode.collectAsState()

    val bgDark = if (isDarkMode) Color(0xFF0F1528) else Color(0xFFF9FAFB)
    val cardBg = if (isDarkMode) Color(0xFF161E38) else Color.White
    val inputBg = if (isDarkMode) Color(0xFF232D48) else Color(0xFFF3F4F6) // A bit lighter than cardBg for inputs
    val textColor = if (isDarkMode) Color.White else Color(0xFF1F2937)
    val subTextColor = if (isDarkMode) Color.Gray else Color.DarkGray
    val accentBlue = Color(0xFF3B82F6)
    val accentRed = Color(0xFFEF4444)

    Surface(modifier = Modifier.fillMaxSize(), color = bgDark) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            
            // Header
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
                        .background(cardBg)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = textColor
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(R.string.profile_title),
                    color = textColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Detalii Personale Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBg)
                    .padding(20.dp)
            ) {
                Text(stringResource(R.string.profile_personal_details), color = textColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                
                Text(stringResource(R.string.profile_email_readonly), color = subTextColor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = user?.email ?: "test@gmail.com",
                    onValueChange = { },
                    readOnly = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = inputBg,
                        unfocusedContainerColor = inputBg,
                        disabledContainerColor = inputBg,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = subTextColor,
                        unfocusedTextColor = subTextColor,
                        disabledTextColor = subTextColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(stringResource(R.string.profile_display_name), color = subTextColor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = displayName,
                    onValueChange = { displayName = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = inputBg,
                        unfocusedContainerColor = inputBg,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (displayName.isBlank()) return@Button
                        isSavingProfile = true
                        val updates = UserProfileChangeRequest.Builder()
                            .setDisplayName(displayName.trim())
                            .build()
                            user?.updateProfile(updates)?.addOnCompleteListener { task ->
                                isSavingProfile = false
                                if (task.isSuccessful) {
                                    Toast.makeText(context, context.getString(R.string.profile_msg_updated), Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, context.getString(R.string.auth_err_general, task.exception?.message ?: ""), Toast.LENGTH_SHORT).show()
                                }
                            }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentBlue),
                    enabled = !isSavingProfile
                ) {
                    if (isSavingProfile) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(stringResource(R.string.profile_save_btn), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Securitate Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBg)
                    .padding(20.dp)
            ) {
                Text(stringResource(R.string.profile_security), color = textColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                
                Text(stringResource(R.string.profile_new_pass), color = subTextColor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    placeholder = { Text(stringResource(R.string.profile_new_pass_hint), color = subTextColor) },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = inputBg,
                        unfocusedContainerColor = inputBg,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(stringResource(R.string.profile_confirm_new_pass), color = subTextColor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = confirmNewPassword,
                    onValueChange = { confirmNewPassword = it },
                    placeholder = { Text(stringResource(R.string.profile_confirm_new_pass_hint), color = subTextColor) },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = inputBg,
                        unfocusedContainerColor = inputBg,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (newPassword.isBlank() || confirmNewPassword.isBlank()) {
                            Toast.makeText(context, context.getString(R.string.auth_err_empty), Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (newPassword != confirmNewPassword) {
                            Toast.makeText(context, context.getString(R.string.auth_err_pass_mismatch), Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        isChangingPassword = true
                        user?.updatePassword(newPassword.trim())?.addOnCompleteListener { task ->
                            isChangingPassword = false
                            if (task.isSuccessful) {
                                newPassword = ""
                                confirmNewPassword = ""
                                Toast.makeText(context, context.getString(R.string.profile_msg_pass_changed), Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, context.getString(R.string.profile_logout_hint), Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentRed),
                    enabled = !isChangingPassword
                ) {
                    if (isChangingPassword) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(stringResource(R.string.profile_change_pass_btn), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
