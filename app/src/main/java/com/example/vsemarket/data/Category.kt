package com.example.vsemarket.data

data class Category(
    val name: String,
    val subcategories: List<Subcategory>,
    val gradient: List<Long>
)

data class Subcategory(
    val name: String,
    val color: Long,
)