package com.example.fooddelivery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddelivery.data.ProductApi
import com.example.fooddelivery.data.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val api: ProductApi) : ViewModel() {
    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults: StateFlow<List<Product>> = _searchResults

    private val _searchError = MutableStateFlow<String?>(null)
    val searchError: StateFlow<String?> = _searchError

    fun searchProducts(query: String) {
        viewModelScope.launch {
            try {
                val results = api.searchProducts(query)
                _searchResults.value = results
                _searchError.value = null
            } catch (e: Exception) {
                _searchResults.value = emptyList()
                _searchError.value = "Не удалось выполнить поиск: ${e.message}"
            }
        }
    }
}
