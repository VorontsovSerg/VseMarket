package com.example.fooddelivery

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fooddelivery.data.Category
import com.example.fooddelivery.data.Order
import com.example.fooddelivery.data.Persistence
import com.example.fooddelivery.data.Product
import com.example.fooddelivery.data.ProductData
import com.example.fooddelivery.data.Subcategory
import com.example.fooddelivery.ui.screens.ProductDetailScreen
import com.example.fooddelivery.utils.ThemeManager
import com.example.fooddelivery.viewmodel.CartViewModel
import com.example.fooddelivery.viewmodel.FavoritesViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import kotlin.random.Random

data class SellerData(
    val userId: String,
    val email: String,
    val firmName: String,
    val description: String
)

class SellerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Включение оффлайн-поддержки Firestore
        val firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()

        setContent {
            val context = LocalContext.current
            val isDarkTheme = remember { mutableStateOf(ThemeManager.isDarkTheme(context)) }
            val userEmail = intent.getStringExtra("userEmail") ?: ""

            ThemeManager.FoodDeliveryTheme(isDarkTheme = isDarkTheme.value) {
                SellerNavigation(userEmail)
            }
        }
    }
}

@Composable
fun SellerNavigation(userEmail: String) {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    var isAuthenticated by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val user = auth.currentUser
        if (user != null) {
            try {
                val firestore = FirebaseFirestore.getInstance()
                val doc = firestore.collection("sellers").document(user.uid).get().await()
                isAuthenticated = doc.exists()
            } catch (e: Exception) {
                Log.e("SellerNavigation", "Ошибка проверки статуса продавца: ${e.message}")
                Toast.makeText(context, "Ошибка проверки статуса продавца", Toast.LENGTH_SHORT).show()
            }
        }
        isLoading = false
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else if (!isAuthenticated) {
        SellerAuthScreen(
            userEmail = userEmail,
            onAuthenticated = { isAuthenticated = true }
        )
    } else {
        NavHost(navController = navController, startDestination = "sellerDashboard") {
            composable("sellerDashboard") { SellerDashboardScreen(navController) }
            composable("addProduct") { AddProductScreen(navController) }
            composable("editProduct/{productId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                if (productId != null) {
                    EditProductScreen(navController, productId)
                } else {
                    Text(
                        text = "Недопустимый ID товара",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            composable("productDetail/{productId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                if (productId != null) {
                    val product = ProductData.products.find { it.id == productId }
                    if (product != null) {
                        // Предполагаем, что CartViewModel и FavoritesViewModel существуют
                        val cartViewModel: CartViewModel = viewModel()
                        val favoritesViewModel: FavoritesViewModel = viewModel()
                        ProductDetailScreen(
                            cartViewModel = cartViewModel,
                            favoritesViewModel = favoritesViewModel,
                            product = product
                        )
                    } else {
                        Text(
                            text = "Товар не найден",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    Text(
                        text = "Недопустимый ID товара",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            composable("orderManagement") { OrderManagementScreen() }
            composable("salesAnalytics") { SalesAnalyticsScreen() }
        }
    }
}

@Composable
fun SellerAuthScreen(userEmail: String, onAuthenticated: () -> Unit) {
    val context = LocalContext.current
    var isRegistering by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf(userEmail) }
    var password by remember { mutableStateOf("") }
    var firmName by remember { mutableStateOf("") }
    var firmDescription by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isRegistering) "Регистрация продавца" else "Вход для продавца",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Электронная почта") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            enabled = userEmail.isEmpty()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        if (isRegistering) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = firmName,
                onValueChange = { firmName = it },
                label = { Text("Название фирмы") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = firmDescription,
                onValueChange = { firmDescription = it },
                label = { Text("Описание фирмы") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (isRegistering) {
                    if (email.isNotEmpty() && password.isNotEmpty() && firmName.isNotEmpty()) {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener {
                                val seller = SellerData(
                                    userId = auth.currentUser?.uid ?: UUID.randomUUID().toString(),
                                    email = email,
                                    firmName = firmName,
                                    description = firmDescription
                                )
                                firestore.collection("sellers").document(seller.userId).set(seller)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show()
                                        onAuthenticated()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("SellerAuthScreen", "Ошибка регистрации: ${e.message}")
                                        Toast.makeText(context, "Ошибка регистрации: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                            .addOnFailureListener { e ->
                                Log.e("SellerAuthScreen", "Ошибка аутентификации: ${e.message}")
                                Toast.makeText(context, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener {
                                val userId = auth.currentUser?.uid ?: return@addOnSuccessListener
                                firestore.collection("sellers").document(userId)
                                    .get()
                                    .addOnSuccessListener { doc ->
                                        if (doc.exists()) {
                                            Toast.makeText(context, "Вход выполнен успешно", Toast.LENGTH_SHORT).show()
                                            onAuthenticated()
                                        } else {
                                            Toast.makeText(context, "Аккаунт не зарегистрирован как продавец", Toast.LENGTH_SHORT).show()
                                            auth.signOut()
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("SellerAuthScreen", "Ошибка проверки данных: ${e.message}")
                                        Toast.makeText(context, "Ошибка проверки: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                            .addOnFailureListener { e ->
                                Log.e("SellerAuthScreen", "Ошибка входа: ${e.message}")
                                Toast.makeText(context, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(context, "Введите email и пароль", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isRegistering) "Зарегистрироваться" else "Войти")
        }

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { isRegistering = !isRegistering }) {
            Text(if (isRegistering) "Уже есть аккаунт? Войти" else "Нет аккаунта? Зарегистрироваться")
        }
    }
}

@Composable
fun SellerDashboardScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Панель продавца",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("addProduct") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Добавить товар")
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.navigate("orderManagement") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Управление заказами")
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.navigate("salesAnalytics") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Аналитика продаж")
        }
        Spacer(modifier = Modifier.height(16.dp))

        ProductList(navController)
    }
}

@Composable
fun ProductList(navController: NavController) {
    val context = LocalContext.current
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val firestore = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        try {
            val snapshot = firestore.collection("products").get().await()
            if (snapshot.isEmpty) {
                ProductData.products.forEach { product ->
                    firestore.collection("products").document(product.id.toString()).set(product).await()
                }
            }
            products = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Product::class.java)?.copy(id = doc.id.toIntOrNull() ?: 0)
            }.ifEmpty { ProductData.products }
            // Синхронизация ProductData.products с Firestore
            (ProductData.products as MutableList<Product>).clear()
            (ProductData.products as MutableList<Product>).addAll(products)
            Log.d("ProductList", "Loaded products: ${products.size}, IDs: ${products.map { it.id }}")
        } catch (e: Exception) {
            Log.e("ProductList", "Ошибка загрузки товаров: ${e.message}")
            errorMessage = "Ошибка загрузки товаров: ${e.message}"
            products = ProductData.products
        }
    }

    if (errorMessage != null) {
        Text(
            text = errorMessage!!,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp)
        )
    } else if (products.isEmpty()) {
        Text(
            text = "Товары отсутствуют",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(16.dp)
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products, key = { it.id }) { product ->
                ProductCard(
                    product = product,
                    onView = { navController.navigate("productDetail/${product.id}") },
                    onEdit = { navController.navigate("editProduct/${product.id}") },
                    onDelete = {
                        // Оптимистическое удаление
                        val oldProducts = products
                        products = products.filter { it.id != product.id }
                        (ProductData.products as MutableList<Product>).removeAll { p: Product -> p.id == product.id }
                        Log.d("ProductList", "Optimistic delete: product ID ${product.id}")

                        firestore.collection("products").document(product.id.toString()).delete()
                            .addOnSuccessListener {
                                Toast.makeText(context, "Товар удалён", Toast.LENGTH_SHORT).show()
                                Log.d("ProductList", "Product deleted from Firestore: ID ${product.id}")
                            }
                            .addOnFailureListener { e ->
                                // Откат при ошибке
                                products = oldProducts
                                (ProductData.products as MutableList<Product>).clear()
                                (ProductData.products as MutableList<Product>).addAll(oldProducts)
                                Log.e("ProductList", "Ошибка удаления товара: ${e.message}")
                                Toast.makeText(context, "Ошибка удаления: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                )
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, onView: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    val context = LocalContext.current
    val orders = try {
        Persistence.loadOrders(context)
    } catch (e: Exception) {
        Log.e("ProductCard", "Ошибка загрузки заказов: ${e.message}")
        emptyList<Order>()
    }
    val purchasedQuantity = orders.sumOf { order ->
        order.items.filter { it.title == product.name }.sumOf { it.quantity }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.medium)
            .padding(8.dp)
            .clickable { onView() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "${product.price} ₽",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Куплено: $purchasedQuantity шт.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (product.isFavorite) "В избранном" else "Не в избранном",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Редактировать", tint = MaterialTheme.colorScheme.onSurface)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
fun AddProductScreen(navController: NavController) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var imageUrls by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var subcategory by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var attributes by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val firestore = FirebaseFirestore.getInstance()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Добавить товар",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Название") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Цена (₽)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = imageUrls,
            onValueChange = { imageUrls = it },
            label = { Text("URL изображений (через запятую или по одному на строку)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            maxLines = 5
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Категория") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = subcategory,
            onValueChange = { subcategory = it },
            label = { Text("Подкатегория") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Описание") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = attributes,
            onValueChange = { attributes = it },
            label = { Text("Атрибуты (ключ:значение, через запятую)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isFavorite,
                onCheckedChange = { isFavorite = it }
            )
            Text(
                text = "Добавить в избранное",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    if (name.isNotBlank() && price.isNotEmpty() && category.isNotBlank() && subcategory.isNotBlank()) {
                        isLoading = true
                        coroutineScope.launch {
                            var newId: Int? = null
                            try {
                                // Фильтрация пустых и некорректных URL
                                val imageList = imageUrls
                                    .split(",", "\n")
                                    .map { it.trim() }
                                    .filter { it.isNotEmpty() && it.startsWith("http") }
                                    .map { it } // List<String> без null

                                val attributeMap = attributes.split(",")
                                    .map { it.trim().split(":") }
                                    .filter { it.size == 2 && it[0].isNotBlank() && it[1].isNotBlank() }
                                    .associate { it[0].trim() to it[1].trim() }

                                // Генерация уникального ID
                                do {
                                    newId = Random.nextInt(1000, 9999)
                                    val docRef = firestore.collection("products").document(newId.toString())
                                    val doc = try {
                                        docRef.get().await()
                                    } catch (e: Exception) {
                                        Log.w("AddProductScreen", "Ошибка проверки ID (возможно оффлайн): ${e.message}")
                                        null // Продолжаем, если оффлайн
                                    }
                                } while (newId?.let { ProductData.products.any { p -> p.id == it } } == true || doc?.exists() == true)

                                val product = Product(
                                    id = newId!!,
                                    name = name,
                                    price = price.toDoubleOrNull() ?: 0.0,
                                    images = imageList,
                                    category = category,
                                    subcategory = subcategory,
                                    description = description,
                                    attributes = attributeMap,
                                    isFavorite = isFavorite
                                )

                                Log.d("AddProductScreen", "Creating product: ID=$newId, Name=${product.name}, Images=${product.images}")

                                // Обновление категорий
                                val existingCategory = ProductData.categories.find { it.name == category }
                                if (existingCategory == null) {
                                    (ProductData.categories as MutableList<Category>).add(
                                        Category(
                                            name = category,
                                            subcategories = mutableListOf(Subcategory(subcategory, 0xFF000000)),
                                            gradient = listOf(0xFF000000, 0xFF666666)
                                        )
                                    )
                                } else if (!existingCategory.subcategories.any { it.name == subcategory }) {
                                    (existingCategory.subcategories as MutableList<Subcategory>).add(
                                        Subcategory(subcategory, 0xFF000000)
                                    )
                                }

                                // Добавление в локальный список
                                (ProductData.products as MutableList<Product>).add(product)
                                Log.d("AddProductScreen", "Product added to ProductData: ${ProductData.products.map { it.id }}")

                                // Сохранение в Firestore (оффлайн-поддержка)
                                firestore.collection("products").document(product.id.toString()).set(product)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Товар добавлен", Toast.LENGTH_SHORT).show()
                                        Log.d("AddProductScreen", "Product saved to Firestore: ID=${product.id}")
                                        navController.popBackStack()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("AddProductScreen", "Ошибка синхронизации с Firestore: ${e.message}")
                                        Toast.makeText(context, "Товар сохранён локально, синхронизация при подключении", Toast.LENGTH_LONG).show()
                                    }

                            } catch (e: Exception) {
                                // Откат при критической ошибке
                                newId?.let { id ->
                                    (ProductData.products as MutableList<Product>).removeAll { p: Product -> p.id == id }
                                }
                                Log.e("AddProductScreen", "Критическая ошибка добавления товара: ${e.message}")
                                Toast.makeText(context, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                            } finally {
                                isLoading = false
                            }
                        }
                    } else {
                        Toast.makeText(context, "Заполните все обязательные поля", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Добавить")
            }
        }
    }
}

@Composable
fun EditProductScreen(navController: NavController, productId: Int) {
    val context = LocalContext.current
    var product by remember { mutableStateOf<Product?>(null) }
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var imageUrls by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var subcategory by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var attributes by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val firestore = FirebaseFirestore.getInstance()

    LaunchedEffect(productId) {
        try {
            // Попробуем найти товар в ProductData.products
            val localProduct = ProductData.products.find { it.id == productId }
            if (localProduct != null) {
                product = localProduct
                name = localProduct.name
                price = localProduct.price.toString()
                imageUrls = localProduct.images.filterNotNull().joinToString(", ")
                category = localProduct.category
                subcategory = localProduct.subcategory
                description = localProduct.description
                attributes = localProduct.attributes.entries.joinToString(", ") { "${it.key}:${it.value}" }
                isFavorite = localProduct.isFavorite
                Log.d("EditProductScreen", "Loaded product from ProductData: ID=$productId, Name=${localProduct.name}")
            } else {
                // Если нет локально, пробуем Firestore
                val doc = firestore.collection("products").document(productId.toString()).get().await()
                product = doc.toObject(Product::class.java)?.copy(id = productId)
                product?.let {
                    name = it.name
                    price = it.price.toString()
                    imageUrls = it.images.filterNotNull().joinToString(", ")
                    category = it.category
                    subcategory = it.subcategory
                    description = it.description
                    attributes = it.attributes.entries.joinToString(", ") { "${it.key}:${it.value}" }
                    isFavorite = it.isFavorite
                    // Добавляем в ProductData.products для синхронизации
                    (ProductData.products as MutableList<Product>).add(it)
                    Log.d("EditProductScreen", "Loaded product from Firestore: ID=$productId, Name=${it.name}")
                } ?: run {
                    errorMessage = "Товар не найден"
                    Log.e("EditProductScreen", "Product not found: ID=$productId")
                }
            }
        } catch (e: Exception) {
            Log.e("EditProductScreen", "Ошибка загрузки товара: ${e.message}")
            errorMessage = "Ошибка загрузки товара: ${e.message}"
        }
    }

    if (errorMessage != null) {
        Text(
            text = errorMessage!!,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp)
        )
    } else if (product == null) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Редактировать товар",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Цена (₽)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = imageUrls,
                onValueChange = { imageUrls = it },
                label = { Text("URL изображений (через запятую или по одному на строку)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 5
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Категория") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = subcategory,
                onValueChange = { subcategory = it },
                label = { Text("Подкатегория") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Описание") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = attributes,
                onValueChange = { attributes = it },
                label = { Text("Атрибуты (ключ:значение, через запятую)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isFavorite,
                    onCheckedChange = { isFavorite = it }
                )
                Text(
                    text = "В избранном",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (name.isNotBlank() && price.isNotEmpty() && category.isNotBlank() && subcategory.isNotBlank()) {
                        val imageList = imageUrls
                            .split(",", "\n")
                            .map { it.trim() }
                            .filter { it.isNotEmpty() && it.startsWith("http") }
                            .map { it }

                        val attributeMap = attributes.split(",")
                            .map { it.trim().split(":") }
                            .filter { it.size == 2 && it[0].isNotBlank() && it[1].isNotBlank() }
                            .associate { it[0].trim() to it[1].trim() }

                        val updatedProduct = Product(
                            id = productId,
                            name = name,
                            price = price.toDoubleOrNull() ?: 0.0,
                            images = imageList,
                            category = category,
                            subcategory = subcategory,
                            description = description,
                            attributes = attributeMap,
                            isFavorite = isFavorite
                        )

                        Log.d("EditProductScreen", "Updating product: ID=$productId, Name=${updatedProduct.name}")

                        val existingCategory = ProductData.categories.find { it.name == category }
                        if (existingCategory == null) {
                            (ProductData.categories as MutableList<Category>).add(
                                Category(
                                    name = category,
                                    subcategories = mutableListOf(Subcategory(subcategory, 0xFF000000)),
                                    gradient = listOf(0xFF000000, 0xFF666666)
                                )
                            )
                        } else if (!existingCategory.subcategories.any { it.name == subcategory }) {
                            (existingCategory.subcategories as MutableList<Subcategory>).add(
                                Subcategory(subcategory, 0xFF000000)
                            )
                        }

                        (ProductData.products as MutableList<Product>).removeAll { p: Product -> p.id == productId }
                        (ProductData.products as MutableList<Product>).add(updatedProduct)
                        Log.d("EditProductScreen", "Product updated in ProductData: ${ProductData.products.map { it.id }}")

                        firestore.collection("products").document(productId.toString()).set(updatedProduct)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Товар обновлён", Toast.LENGTH_SHORT).show()
                                Log.d("EditProductScreen", "Product updated in Firestore: ID=$productId")
                                navController.popBackStack()
                            }
                            .addOnFailureListener { e ->
                                Log.e("EditProductScreen", "Ошибка обновления товара: ${e.message}")
                                Toast.makeText(context, "Товар обновлён локально, синхронизация при подключении", Toast.LENGTH_LONG).show()
                            }
                    } else {
                        Toast.makeText(context, "Заполните все обязательные поля", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить")
            }
        }
    }
}

@Composable
fun OrderManagementScreen() {
    val context = LocalContext.current
    var orders by remember { mutableStateOf<List<Order>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            orders = Persistence.loadOrders(context)
        } catch (e: Exception) {
            Log.e("OrderManagementScreen", "Ошибка загрузки заказов: ${e.message}")
            errorMessage = "Ошибка загрузки заказов: ${e.message}"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Управление заказами",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        when {
            errorMessage != null -> {
                Text(
                    text = errorMessage!!,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            orders.isEmpty() -> {
                Text(
                    text = "Заказы отсутствуют",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(orders, key = { it.orderNumber }) { order ->
                        OrderManagementCard(order = order) {
                            try {
                                orders = Persistence.loadOrders(context)
                            } catch (e: Exception) {
                                Log.e("OrderManagementCard", "Ошибка обновления заказов: ${e.message}")
                                errorMessage = "Ошибка обновления заказов: ${e.message}"
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderManagementCard(order: Order, onStatusUpdated: () -> Unit) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val statuses = listOf("В обработке", "Подтверждён", "В пути", "Доставлен", "Отменён")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.medium)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = "Заказ: ${order.orderNumber}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Итого: ${order.totalPrice} ₽",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Статус: ${order.status}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box {
                Button(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Изменить статус")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    statuses.forEach { status ->
                        DropdownMenuItem(
                            text = { Text(status) },
                            onClick = {
                                try {
                                    val updatedOrders = Persistence.loadOrders(context).map {
                                        if (it.orderNumber == order.orderNumber) it.copy(status = status) else it
                                    }
                                    Persistence.saveOrders(context, updatedOrders)
                                    onStatusUpdated()
                                    expanded = false
                                } catch (e: Exception) {
                                    Log.e("OrderManagementCard", "Ошибка изменения статуса: ${e.message}")
                                    Toast.makeText(context, "Ошибка изменения статуса: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SalesAnalyticsScreen() {
    val context = LocalContext.current
    var orders by remember { mutableStateOf<List<Order>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            orders = Persistence.loadOrders(context)
        } catch (e: Exception) {
            Log.e("SalesAnalyticsScreen", "Ошибка загрузки заказов: ${e.message}")
            errorMessage = "Ошибка загрузки аналитики: ${e.message}"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Аналитика продаж",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        when {
            errorMessage != null -> {
                Text(
                    text = errorMessage!!,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            orders.isEmpty() -> {
                Text(
                    text = "Данные о продажах отсутствуют",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                val totalSales = orders.sumOf { it.totalPrice }
                val completedOrders = orders.count { it.status == "Доставлен" }
                val cancelledOrders = orders.count { it.status == "Отменён" }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Общая выручка: $totalSales ₽",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Завершённые заказы: $completedOrders",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Отменённые заказы: $cancelledOrders",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}
