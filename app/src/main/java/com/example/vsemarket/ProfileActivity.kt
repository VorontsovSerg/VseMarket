package com.example.vsemarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.vsemarket.data.Persistence
import com.example.vsemarket.data.ProfileData
import com.example.vsemarket.ui.screens.EditProfileScreen
import com.example.vsemarket.utils.ThemeManager
import java.util.UUID

/**
 * Активность профиля пользователя.
 * Позволяет просматривать и редактировать информацию о пользователе.
 */

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val isDarkTheme = remember { mutableStateOf(ThemeManager.isDarkTheme(context)) }

            ThemeManager.FoodDeliveryTheme(isDarkTheme = isDarkTheme.value) {
                ProfileScreen(
                    textSize = 18.sp,
                    isDarkTheme = isDarkTheme.value,
                    onThemeChange = { newTheme ->
                        isDarkTheme.value = newTheme
                        ThemeManager.saveTheme(context, newTheme)
                        ThemeManager.restartActivity(this)
                    }
                )
            }
        }
    }
}

@Composable
fun ProfileScreen(textSize: TextUnit, isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val initialProfile = remember {
        Persistence.loadProfile(context) ?: ProfileData(
            userId = UUID.randomUUID().toString(),
            userName = "Гость",
            email = "example@email.com",
            phone = null
        )
    }
    var profile by remember { mutableStateOf(initialProfile) }

    NavHost(navController = navController, startDestination = "profile") {
        composable("profile") {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = { navController.navigate("editProfile") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    if (profile.avatarUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(profile.avatarUri),
                            contentDescription = "Profile Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = "Нет аватара",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("Имя: ")
                            pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                            append(profile.userName)
                            pop()
                        },
                        style = TextStyle(fontSize = textSize, color = MaterialTheme.colorScheme.onSurface)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = buildAnnotatedString {
                            append("Email: ")
                            pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                            append(profile.email)
                            pop()
                        },
                        style = TextStyle(fontSize = textSize, color = MaterialTheme.colorScheme.onSurface)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = buildAnnotatedString {
                            append("Телефон: ")
                            pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                            append(profile.phone ?: "Не указан")
                            pop()
                        },
                        style = TextStyle(fontSize = textSize, color = MaterialTheme.colorScheme.onSurface)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { checked ->
                            onThemeChange(checked)
                        },
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Text(
                        text = "Темная тема",
                        style = TextStyle(fontSize = textSize, color = MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
            }
        }
        composable("editProfile") {
            EditProfileScreen(
                navController = navController,
                initialProfile = profile,
                onProfileUpdated = { updatedProfile ->
                    profile = updatedProfile
                }
            )
        }
    }
}