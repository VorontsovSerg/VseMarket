package com.example.fooddelivery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddelivery.data.Category
import com.example.fooddelivery.data.ProductApi
import com.example.fooddelivery.data.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatalogViewModel(private val api: ProductApi) : ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _productsBySubcategory = MutableStateFlow<List<Product>>(emptyList())
    val productsBySubcategory: StateFlow<List<Product>> = _productsBySubcategory.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _categories.value = api.getCategories() // Вызов suspend-функции внутри корутины
        }
    }

    fun getProductsBySubcategory(categoryName: String, subcategoryName: String): StateFlow<List<Product>> {
        viewModelScope.launch {
            _productsBySubcategory.value = api.getProductsBySubcategory(categoryName, subcategoryName) // Вызов suspend-функции внутри корутины
        }
        return productsBySubcategory
    }
}
