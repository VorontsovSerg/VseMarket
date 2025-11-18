package com.example.fooddelivery

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fooddelivery.data.CartItem
import com.example.fooddelivery.data.Order
import com.example.fooddelivery.data.Persistence
import com.example.fooddelivery.utils.ThemeManager
import com.example.fooddelivery.viewmodel.CartViewModel
import kotlin.random.Random

class PaymentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContent {
            val context = LocalContext.current
            val isDarkTheme = remember { mutableStateOf(ThemeManager.isDarkTheme(context)) }
            val totalPrice = intent.getDoubleExtra("totalPrice", 0.0)
            val cartItems = Persistence.loadCart(context)

            ThemeManager.FoodDeliveryTheme(isDarkTheme = isDarkTheme.value) {
                PaymentNavigation(totalPrice, cartItems)
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
fun PaymentNavigation(totalPrice: Double, cartItems: List<CartItem>) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { PaymentMainScreen(navController, totalPrice, cartItems) }
        composable("sbp") { SbpPaymentScreen(totalPrice, cartItems) }
        composable("card") { CardPaymentScreen(totalPrice, cartItems) }
    }
}

@Composable
fun PaymentMainScreen(navController: NavController, totalPrice: Double, cartItems: List<CartItem>) {
    var address by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("card") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Оплата",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Адрес доставки") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Способ оплаты",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { paymentMethod = "sbp" }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.sbp),
                contentDescription = "СБП",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "СБП",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.weight(1f))
            RadioButton(
                selected = paymentMethod == "sbp",
                onClick = { paymentMethod = "sbp" },
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { paymentMethod = "card" }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.CreditCard,
                contentDescription = "Карта",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Карта",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.weight(1f))
            RadioButton(
                selected = paymentMethod == "card",
                onClick = { paymentMethod = "card" },
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Итого: $totalPrice ₽",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (paymentMethod == "sbp") {
                    navController.navigate("sbp")
                } else {
                    navController.navigate("card")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.background
            ),
            shape = CircleShape
        ) {
            Text(
                text = "Оплатить",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun SbpPaymentScreen(totalPrice: Double, cartItems: List<CartItem>) {
    val context = LocalContext.current
    val banks = listOf(
        Bank("Сбербанк", R.drawable.sber),
        Bank("Т-Банк", R.drawable.tbank),
        Bank("Альфа-Банк", R.drawable.alpha),
        Bank("ВТБ", R.drawable.vtb)
    )
    var selectedBank by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Оплата через СБП",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(banks) { bank ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedBank = bank.name }
                        .padding(8.dp)
                        .border(
                            1.dp,
                            if (selectedBank == bank.name) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            MaterialTheme.shapes.small
                        )
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = bank.iconResId),
                        contentDescription = bank.name,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = bank.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    RadioButton(
                        selected = selectedBank == bank.name,
                        onClick = { selectedBank = bank.name },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Итого: $totalPrice ₽",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (cartItems.isEmpty()) {
                    showNotification(
                        context,
                        "Ошибка заказа",
                        "Корзина пуста, заказ не создан",
                        Random.nextInt()
                    )
                    return@Button
                }
                val orderNumber = "#${Random.nextLong(10000000000, 99999999999)}"
                val order = Order(orderNumber, totalPrice, "В обработке", cartItems)
                val orders = Persistence.loadOrders(context).toMutableList()
                orders.add(order)
                Persistence.saveOrders(context, orders)
                CartViewModel(context).clearCart()
                Persistence.saveCart(context, emptyList()) // Явная очистка корзины
                showNotification(
                    context,
                    "Заказ создан",
                    "Заказ $orderNumber создан, корзина очищена",
                    Random.nextInt()
                )
                (context as PaymentActivity).finish()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.background
            ),
            shape = CircleShape
        ) {
            Text(
                text = "Оплатить",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

data class Bank(val name: String, val iconResId: Int)

@Composable
fun CardPaymentScreen(totalPrice: Double, cartItems: List<CartItem>) {
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var cardHolderName by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Оплата картой",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = cardNumber,
            onValueChange = { cardNumber = it },
            label = { Text("Номер карты") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = expiryDate,
            onValueChange = { expiryDate = it },
            label = { Text("Срок действия (MM/YY)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = cvv,
            onValueChange = { cvv = it },
            label = { Text("CVV") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = cardHolderName,
            onValueChange = { cardHolderName = it },
            label = { Text("Имя владельца") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Итого: $totalPrice ₽",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (cartItems.isEmpty()) {
                    showNotification(
                        context,
                        "Ошибка заказа",
                        "Корзина пуста, заказ не создан",
                        Random.nextInt()
                    )
                    return@Button
                }
                val orderNumber = "#${Random.nextLong(10000000000, 99999999999)}"
                val order = Order(orderNumber, totalPrice, "В обработке", cartItems)
                val orders = Persistence.loadOrders(context).toMutableList()
                orders.add(order)
                Persistence.saveOrders(context, orders)
                CartViewModel(context).clearCart()
                Persistence.saveCart(context, emptyList()) // Явная очистка корзины
                showNotification(
                    context,
                    "Заказ создан",
                    "Заказ $orderNumber создан, корзина очищена",
                    Random.nextInt()
                )
                (context as PaymentActivity).finish()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.background
            ),
            shape = CircleShape
        ) {
            Text(
                text = "Оплатить",
                style = MaterialTheme.typography.bodyLarge
            )
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
        .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(notificationId, builder.build())
}
