package com.example.fooddelivery

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fooddelivery.data.ProductApiImpl
import com.example.fooddelivery.ui.components.BottomNavigationBar
import com.example.fooddelivery.ui.components.SearchBar
import com.example.fooddelivery.ui.components.SearchResults
import com.example.fooddelivery.ui.screens.*
import com.example.fooddelivery.utils.ThemeManager
import com.example.fooddelivery.viewmodel.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val isDarkTheme = remember { mutableStateOf(ThemeManager.isDarkTheme(context)) }

            ThemeManager.FoodDeliveryTheme(isDarkTheme = isDarkTheme.value) {
                AuthCheckScreen()
            }
        }
    }
}

@Composable
fun AuthCheckScreen() {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    var isCheckingAuth by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (auth.currentUser == null) {
            context.startActivity(Intent(context, AuthActivity::class.java))
            (context as MainActivity).finish()
        } else {
            isCheckingAuth = false
        }
    }

    if (isCheckingAuth) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else {
        FoodDeliveryApp()
    }
}

@Composable
fun FoodDeliveryApp() {
    val navController = rememberNavController()
    var searchQuery by remember { mutableStateOf("") }
    var currentQuery by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isSearchFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val api = ProductApiImpl(context)
    val mainViewModel = MainViewModel(api)
    val catalogViewModel = CatalogViewModel(api)
    val favoritesViewModel = FavoritesViewModel(context)
    val cartViewModel = CartViewModel(context)
    val searchViewModel = SearchViewModel(api)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SearchBar(
                searchQuery = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { query ->
                    scope.launch {
                        searchViewModel.searchProducts(query)
                        currentQuery = query
                        searchQuery = ""
                        isSearchFocused = false
                        focusManager.clearFocus()
                        navController.navigate("search")
                    }
                },
                onFocusChange = { focused ->
                    isSearchFocused = focused
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val newProducts by mainViewModel.newProducts.collectAsState()
                val recommendedProducts by mainViewModel.recommendedProducts.collectAsState()
                val categories by catalogViewModel.categories.collectAsState()
                val subcategories = categories.flatMap { it.subcategories }

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(mainViewModel, navController, favoritesViewModel)
                    }
                    composable("catalog") { CatalogScreen(catalogViewModel, navController) }
                    composable("favorites") { FavoritesScreen(favoritesViewModel, navController) }
                    composable("cart") { CartScreen(cartViewModel) }
                    composable("profile") { ProfileScreen(navController) }
                    composable("search") {
                        SearchResults(
                            query = currentQuery,
                            viewModel = searchViewModel,
                            subcategories = subcategories,
                            onProductClick = { product ->
                                scope.launch {
                                    searchViewModel.searchProducts(currentQuery)
                                    navController.navigate("product/${product.id}")
                                }
                            }
                        )
                    }
                    composable("subcategories/{categoryName}") { backStackEntry ->
                        val categoryName = backStackEntry.arguments?.getString("categoryName")
                        val category = categories.find { it.name == categoryName }
                        category?.let { SubcategoryScreen(it, navController) }
                            ?: Text("Категория не найдена")
                    }
                    composable("products/{categoryName}/{subcategoryName}") { backStackEntry ->
                        val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
                        val subcategoryName = backStackEntry.arguments?.getString("subcategoryName") ?: ""
                        ProductsBySubcategoryScreen(catalogViewModel, categoryName, subcategoryName, navController)
                    }
                    composable("product/{productId}") { backStackEntry ->
                        val productId = backStackEntry.arguments?.getString("productId")?.toInt()
                        val product = newProducts.find { it.id == productId }
                            ?: recommendedProducts.find { it.id == productId }
                        product?.let { ProductDetailScreen(it, cartViewModel, favoritesViewModel) }
                            ?: Text("Товар не найден")
                    }
                }

                if (isSearchFocused) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                            .clickable(
                                onClick = {
                                    isSearchFocused = false
                                    focusManager.clearFocus()
                                },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            )
                    )
                }
            }

            BottomNavigationBar(navController) { route ->
                if (route == "home") {
                    currentQuery = ""
                    searchQuery = ""
                }
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        }
    }

    BackHandler(enabled = isSearchFocused) {
        isSearchFocused = false
        focusManager.clearFocus()
    }
}

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        context.startActivity(Intent(context, ProfileActivity::class.java))
        (context as ComponentActivity).finish()
    }
}
