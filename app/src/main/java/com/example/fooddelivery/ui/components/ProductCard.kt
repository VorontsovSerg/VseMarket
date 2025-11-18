package com.example.fooddelivery.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.fooddelivery.data.Product
import com.example.fooddelivery.data.Subcategory

/**
 * Компонент карты товара.
 * Карта которая привязана будет к определенному товару и будет настроена.
 */

@Composable
fun ProductCard(
    product: Product,
    subcategories: List<Subcategory>,
    onFavoriteClick: (Product) -> Unit,
    onClick: () -> Unit
) {
    val subcategory = subcategories.find { it.name == product.subcategory }
    val cardColor = subcategory?.color?.let { Color(it) } ?: Color.Gray

    Box(
        modifier = Modifier
            .size(width = 150.dp, height = 200.dp)
            .background(cardColor, MaterialTheme.shapes.large)
            .clickable { onClick() } // Переход на ProductDetailScreen
            .padding(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color.White)
            ) {
                AsyncImage(
                    model = product.images.firstOrNull(),
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                    error = painterResource(androidx.appcompat.R.drawable.abc_ic_menu_paste_mtrl_am_alpha),
                    placeholder = painterResource(androidx.appcompat.R.drawable.abc_ic_menu_paste_mtrl_am_alpha)
                )
                IconButton(
                    onClick = { onFavoriteClick(product) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = if (product.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (product.isFavorite) Color.Red else Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                product.name,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                maxLines = 2
            )
            Text(
                "${product.price} ₽",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )
        }
    }
}
