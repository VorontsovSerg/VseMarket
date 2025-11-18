package com.example.fooddelivery.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fooddelivery.data.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoritesViewModel(private val context: Context) : ViewModel() {
    private val _favorites = MutableStateFlow<List<Product>>(emptyList())
    val favorites: StateFlow<List<Product>> = _favorites

    init {
        loadFavorites()
    }

    fun toggleFavorite(product: Product) {
        val currentFavorites = _favorites.value.toMutableList()
        if (product.isFavorite) {
            currentFavorites.removeAll { it.id == product.id }
            product.isFavorite = false
        } else {
            currentFavorites.add(product.copy(isFavorite = true))
            product.isFavorite = true
        }
        _favorites.value = currentFavorites
        saveFavorites()
    }

    private fun saveFavorites() {
        val prefs = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(_favorites.value)
        prefs.edit { putString("favorites_list", json) }
    }

    private fun loadFavorites() {
        val prefs = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString("favorites_list", null)
        val type = object : TypeToken<List<Product>>() {}.type
        _favorites.value = if (json != null) gson.fromJson(json, type) else emptyList()
    }
}
