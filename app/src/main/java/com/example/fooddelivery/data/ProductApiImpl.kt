package com.example.fooddelivery.data

import android.content.Context

/**
 * Реализация интерфейса ProductApi.
 * Отвечает за получение данных о товарах (локально или через сеть).
 */

class ProductApiImpl(private val context: Context) : ProductApi {
    override suspend fun getNewProducts(): List<Product> {
        return ProductData.products.filter { it.id in listOf(1, 2) }
    }

    override suspend fun getRecommendedProducts(): List<Product> {
        return ProductData.products.filter { it.id in listOf(3, 4) }
    }

    override suspend fun getCategories(): List<Category> {
        return ProductData.categories
    }

    override suspend fun getProductsBySubcategory(categoryName: String, subcategoryName: String): List<Product> {
        return ProductData.products.filter { it.category == categoryName && it.subcategory == subcategoryName }
    }

    override suspend fun searchProducts(query: String): List<Product> {
        return ProductData.products.filter { it.name.contains(query, ignoreCase = true) }
    }

    override suspend fun getProducts(): List<Product> {
        return ProductData.products
    }
}
