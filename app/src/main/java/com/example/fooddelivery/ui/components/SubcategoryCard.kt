package com.example.fooddelivery.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fooddelivery.data.Subcategory

/**
 * Компонент карты подкатегории.
 * Карта которая привязана будет к определенной подкатегории и будет настроена.
 */

@Composable
fun SubcategoryCard(subcategory: Subcategory, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(width = 160.dp, height = 200.dp) // Фиксированный размер, как у ProductCard
            .background(Color(subcategory.color), MaterialTheme.shapes.large) // Та же форма
            .clickable { onClick() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            subcategory.name,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
    }
}
