package com.example.fooddelivery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddelivery.data.ProductApi
import com.example.fooddelivery.data.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val api: ProductApi) : ViewModel() {
    private val _newProducts = MutableStateFlow<List<Product>>(emptyList())
    val newProducts: StateFlow<List<Product>> = _newProducts

    private val _recommendedProducts = MutableStateFlow<List<Product>>(emptyList())
    val recommendedProducts: StateFlow<List<Product>> = _recommendedProducts

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            try {
                val products = api.getProducts()
                _newProducts.value = products.take(5) // Первые 5 как новинки
                _recommendedProducts.value = products.drop(5) // Остальные как рекомендации
            } catch (e: Exception) {
                // Обработка ошибок
            }
        }
    }

    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            val updatedNewProducts = _newProducts.value.map {
                if (it.id == product.id) it.copy(isFavorite = !it.isFavorite) else it
            }
            val updatedRecommendedProducts = _recommendedProducts.value.map {
                if (it.id == product.id) it.copy(isFavorite = !it.isFavorite) else it
            }
            _newProducts.value = updatedNewProducts
            _recommendedProducts.value = updatedRecommendedProducts
        }
    }
}
