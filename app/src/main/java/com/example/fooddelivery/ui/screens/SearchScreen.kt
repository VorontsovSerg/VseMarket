package com.example.fooddelivery.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fooddelivery.data.ProductData
import com.example.fooddelivery.ui.components.ProductCard
import com.example.fooddelivery.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

/**
 * Фрагмент результатов поиска.
 * Показывает результаты поиска по запросу.
 */

@Composable
fun SearchScreen(viewModel: SearchViewModel, query: String, navController: NavController) {
    val searchResults = viewModel.searchResults.collectAsState().value
    val searchError = viewModel.searchError.collectAsState().value
    val scope = rememberCoroutineScope()
    var lastQuery by remember { mutableStateOf(query) }
    val subcategories = ProductData.categories.flatMap { it.subcategories }

    LaunchedEffect(query) {
        if (query.isNotEmpty()) {
            lastQuery = query
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Результаты поиска для \"$query\"",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        when {
            searchError != null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ошибка поиска: $searchError",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.searchProducts(lastQuery)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Text(
                            text = "Обновить",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            searchResults.isEmpty() && query.isNotEmpty() -> {
                Text(
                    text = "Ничего не найдено",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(searchResults) { product ->
                        Box(
                            modifier = Modifier
                                .size(width = 150.dp, height = 200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ProductCard(
                                product = product,
                                subcategories = subcategories,
                                onFavoriteClick = { /* Не используется здесь */ },
                                onClick = { navController.navigate("product/${product.id}") }
                            )
                        }
                    }
                }
            }
        }
    }
}
