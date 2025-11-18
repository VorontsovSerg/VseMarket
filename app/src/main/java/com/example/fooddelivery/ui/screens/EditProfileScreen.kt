package com.example.fooddelivery.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.fooddelivery.data.Persistence
import com.example.fooddelivery.data.ProfileData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import java.util.regex.Pattern

@Composable
fun EditProfileScreen(
    navController: NavController,
    initialProfile: ProfileData,
    onProfileUpdated: (ProfileData) -> Unit
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val editNavController = rememberNavController()
    var userName by remember { mutableStateOf(initialProfile.userName) }
    var email by remember { mutableStateOf(initialProfile.email) }
    var phone by remember { mutableStateOf(initialProfile.phone ?: "") }
    var avatarUri by remember { mutableStateOf(initialProfile.avatarUri) }
    var isLoading by remember { mutableStateOf(false) }

    // Регулярное выражение для проверки номера телефона
    val phonePattern = Pattern.compile("^((\\+7|8)[0-9]{10})\$")

    // Сбрасываем email, если он был изменен, но не подтвержден
    LaunchedEffect(initialProfile.email) {
        if (!initialProfile.isEmailVerified && email != initialProfile.email) {
            email = initialProfile.email
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        avatarUri = uri?.toString()
    }

    NavHost(navController = editNavController, startDestination = "edit") {
        composable("edit") {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Редактировать профиль",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable { launcher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (avatarUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(avatarUri),
                            contentDescription = "Profile Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = "Выбрать аватар",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("Имя") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Телефон (+7 или 8)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text("Отмена")
                    }
                    Button(
                        onClick = {
                            if (userName.isEmpty() || email.isEmpty()) {
                                Toast.makeText(context, "Имя и email обязательны", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            val normalizedPhone = if (phone.startsWith("8")) {
                                "+7${phone.substring(1)}"
                            } else {
                                phone
                            }
                            if (phone.isNotEmpty() && !phonePattern.matcher(normalizedPhone).matches()) {
                                Toast.makeText(context, "Номер телефона должен начинаться с +7 или 8 и содержать 11 цифр", Toast.LENGTH_LONG).show()
                                return@Button
                            }

                            val needsEmailVerification = email != initialProfile.email || !initialProfile.isEmailVerified

                            if (needsEmailVerification) {
                                isLoading = true
                                auth.currentUser?.updateEmail(email)?.addOnCompleteListener { task ->
                                    isLoading = false
                                    if (task.isSuccessful) {
                                        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                                            Toast.makeText(context, "Письмо для подтверждения email отправлено", Toast.LENGTH_LONG).show()
                                            editNavController.navigate("email_verification")
                                        }
                                    } else {
                                        Toast.makeText(context, "Ошибка обновления email: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } else {
                                val updatedProfile = ProfileData(
                                    userId = initialProfile.userId,
                                    userName = userName,
                                    email = email,
                                    phone = normalizedPhone.ifEmpty { null },
                                    avatarUri = avatarUri,
                                    isEmailVerified = initialProfile.isEmailVerified
                                )
                                auth.currentUser?.updateProfile(
                                    UserProfileChangeRequest.Builder()
                                        .setDisplayName(userName)
                                        .setPhotoUri(avatarUri?.let { Uri.parse(it) })
                                        .build()
                                )
                                Persistence.saveProfile(context, updatedProfile)
                                onProfileUpdated(updatedProfile)
                                navController.popBackStack()
                            }
                        },
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Сохранить")
                    }
                }
            }
        }
        composable("email_verification") {
            EmailVerificationScreen(
                navController = editNavController,
                onVerificationSuccess = {
                    val updatedProfile = ProfileData(
                        userId = initialProfile.userId,
                        userName = userName,
                        email = email,
                        phone = phone.ifEmpty { null },
                        avatarUri = avatarUri,
                        isEmailVerified = true
                    )
                    auth.currentUser?.updateProfile(
                        UserProfileChangeRequest.Builder()
                            .setDisplayName(userName)
                            .setPhotoUri(avatarUri?.let { Uri.parse(it) })
                            .build()
                    )
                    Persistence.saveProfile(context, updatedProfile)
                    onProfileUpdated(updatedProfile)
                    navController.popBackStack()
                },
                onBack = { editNavController.popBackStack() }
            )
        }
    }
}

@Composable
fun EmailVerificationScreen(
    navController: NavController,
    onVerificationSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Подтверждение Email",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Проверьте ваш email и перейдите по ссылке для подтверждения",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Назад")
            }
            Button(
                onClick = {
                    isLoading = true
                    auth.currentUser?.reload()?.addOnCompleteListener { task ->
                        isLoading = false
                        if (task.isSuccessful && auth.currentUser?.isEmailVerified == true) {
                            onVerificationSuccess()
                        } else {
                            Toast.makeText(context, "Email еще не подтвержден", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Подтвердить")
            }
        }
    }
}
