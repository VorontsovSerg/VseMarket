package com.example.fooddelivery.data

/**
 * Модель категории товаров.
 * Используется для классификации товаров по группам.
 */

data class Category(
    val name: String,
    val subcategories: List<Subcategory>,
    val gradient: List<Long>
)

data class Subcategory(
    val name: String,
    val color: Long,
)
