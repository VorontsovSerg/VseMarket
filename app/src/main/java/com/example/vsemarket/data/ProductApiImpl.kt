package com.example.vsemarket.data

import android.content.Context

class FoodApiImpl(private val context: Context) : FoodApi {
    override suspend fun getNewProducts(): List<Product> {
        return FoodData.products.filter { it.id in listOf(1, 2) }
    }

    override suspend fun getRecommendedProducts(): List<Product> {
        return FoodData.products.filter { it.id in listOf(3, 4) }
    }

    override suspend fun getCategories(): List<Category> {
        return FoodData.categories
    }

    override suspend fun getProductsBySubcategory(categoryName: String, subcategoryName: String): List<Product> {
        return FoodData.products.filter { it.category == categoryName && it.subcategory == subcategoryName }
    }

    override suspend fun searchProducts(query: String): List<Product> {
        return FoodData.products.filter { it.name.contains(query, ignoreCase = true) }
    }

    override suspend fun getProducts(): List<Product> {
        return FoodData.products
    }
}