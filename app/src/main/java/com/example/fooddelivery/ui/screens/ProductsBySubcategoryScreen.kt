package com.example.fooddelivery.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fooddelivery.data.ProductData
import com.example.fooddelivery.ui.components.ProductCard
import com.example.fooddelivery.viewmodel.CatalogViewModel

/**
 * Фрагмент с товарами подкатегории.
 * Показывает товары определенной подкатегории.
 */

@Composable
fun ProductsBySubcategoryScreen(
    viewModel: CatalogViewModel,
    categoryName: String,
    subcategoryName: String,
    navController: NavController
) {
    val products = viewModel.getProductsBySubcategory(categoryName, subcategoryName).collectAsState().value
    val subcategories = ProductData.categories.flatMap { it.subcategories }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "$categoryName - $subcategoryName",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (products.isEmpty()) {
            Text(
                text = "Нет товаров в этой подкатегории",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products) { product ->
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
