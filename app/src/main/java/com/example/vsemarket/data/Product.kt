package com.example.vsemarket.data

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val images: List<String?>,
    val category: String,
    val subcategory: String,
    val description: String,
    val attributes: Map<String, String>,
    var isFavorite: Boolean = false
)