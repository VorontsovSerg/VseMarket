package com.example.fooddelivery

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.fooddelivery.data.Order
import com.example.fooddelivery.data.Persistence
import com.example.fooddelivery.data.ProfileData
import com.example.fooddelivery.ui.screens.EditProfileScreen
import com.example.fooddelivery.utils.ThemeManager
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID
import kotlin.random.Random

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Уведомления о заказах"
            val descriptionText = "Уведомления об обновлениях заказов"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("order_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

@Composable
fun ProfileScreen(
    textSize: TextUnit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val initialProfile = remember {
        Persistence.loadProfile(context) ?: ProfileData(
            userId = UUID.randomUUID().toString(),
            userName = "Гость",
            email = "example@email.com",
            phone = null,
            avatarUri = null,
            isEmailVerified = false
        ).also {
            Log.d("ProfileScreen", "Initial profile loaded: avatarUri = ${it.avatarUri}")
        }
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
                    Button(
                        onClick = {
                            val user = auth.currentUser
                            if (user == null) {
                                context.startActivity(Intent(context, AuthActivity::class.java))
                            } else {
                                val intent = Intent(context, SellerActivity::class.java).apply {
                                    putExtra("userEmail", user.email)
                                }
                                context.startActivity(intent)
                            }
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .wrapContentWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF2196F3), // Синий
                                            Color(0xFF21CBF3)  // Голубой
                                        ),
                                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                                        end = androidx.compose.ui.geometry.Offset(100f, 100f)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Панель продавца",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
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
                                contentDescription = "Аватар профиля",
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
                    IconButton(
                        onClick = { navController.navigate("editProfile") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Редактировать профиль",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
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
                            append(" (")
                            append(if (profile.isEmailVerified) "подтвержден" else "не подтвержден")
                            append(")")
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

                    Button(
                        onClick = { navController.navigate("orders") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Мои Заказы")
                    }
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

                Button(
                    onClick = {
                        auth.signOut()
                        Persistence.clearProfile(context)
                        context.startActivity(Intent(context, AuthActivity::class.java))
                        (context as ProfileActivity).finish()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Выйти")
                }
            }
        }
        composable("editProfile") {
            EditProfileScreen(
                navController = navController,
                initialProfile = profile,
                onProfileUpdated = { updatedProfile ->
                    profile = updatedProfile
                    Persistence.saveProfile(context, updatedProfile) // Сохраняем профиль
                    Log.d("ProfileScreen", "Profile updated: avatarUri = ${updatedProfile.avatarUri}")
                }
            )
        }
        composable("orders") {
            OrdersScreen(navController)
        }
    }
}

@Composable
fun OrdersScreen(navController: NavController) {
    val context = LocalContext.current
    var orders by remember { mutableStateOf(Persistence.loadOrders(context)) }
    var showDialog by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Мои Заказы",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (orders.isEmpty()) {
            Text(
                text = "Нет заказов",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(orders) { order ->
                    OrderCard(
                        order = order,
                        onDelete = { showDialog = order.orderNumber }
                    )
                }
            }
        }

        if (showDialog != null) {
            AlertDialog(
                onDismissRequest = { showDialog = null },
                title = { Text("Подтверждение") },
                text = { Text("Вы действительно хотите удалить заказ $showDialog?") },
                confirmButton = {
                    Button(
                        onClick = {
                            val orderNumber = showDialog!!
                            orders = orders.filter { it.orderNumber != orderNumber }
                            Persistence.saveOrders(context, orders)
                            showNotification(
                                context,
                                "Заказ удален",
                                "Заказ $orderNumber отменен",
                                Random.nextInt()
                            )
                            showDialog = null
                        }
                    ) {
                        Text("Удалить")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = null }) {
                        Text("Отмена")
                    }
                }
            )
        }
    }
}

@Composable
fun OrderCard(order: Order, onDelete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.medium)
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Заказ: ${order.orderNumber}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f)
                )
                if (order.status == "В обработке") {
                    Box {
                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "Дополнительные опции",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Удалить", color = MaterialTheme.colorScheme.onSurface) },
                                onClick = {
                                    onDelete()
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
            Text(
                text = "Сумма: ${order.totalPrice} ₽",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Статус: ${order.status}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Товары:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            order.items.forEach { item ->
                Text(
                    text = "- ${item.title} (${item.quantity} шт.)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

private fun showNotification(context: Context, title: String, content: String, notificationId: Int) {
    val intent = Intent(context, ProfileActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val builder = NotificationCompat.Builder(context, "order_channel")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(notificationId, builder.build())
}
