package com.example.fooddelivery.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),   // Для поисковой строки
    medium = RoundedCornerShape(12.dp), // Для белых масок в карточках
    large = RoundedCornerShape(16.dp)   // Для внешних карточек
)
