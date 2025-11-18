package com.example.fooddelivery.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.fooddelivery.data.CartItem
import com.example.fooddelivery.data.Persistence
import com.example.fooddelivery.data.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartViewModel(private val context: Context? = null) : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

    init {
        context?.let { loadCart(it) }
    }

    fun addToCart(product: Product) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.id == product.id }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            currentItems.add(
                CartItem(
                    id = product.id,
                    title = product.name,
                    price = product.price.toInt(),
                    image = product.images.filterNotNull().firstOrNull() ?: "",
                    quantity = 1
                )
            )
        }
        _cartItems.value = currentItems
        updateTotalPrice()
        context?.let { Persistence.saveCart(it, currentItems) }
    }

    fun updateQuantity(id: Int, delta: Int) {
        val currentItems = _cartItems.value.toMutableList()
        val item = currentItems.find { it.id == id }
        if (item != null) {
            item.quantity += delta
            if (item.quantity <= 0) {
                currentItems.remove(item)
            }
        }
        _cartItems.value = currentItems
        updateTotalPrice()
        context?.let { Persistence.saveCart(it, currentItems) }
    }

    fun removeFromCart(id: Int) {
        val currentItems = _cartItems.value.toMutableList()
        currentItems.removeAll { it.id == id }
        _cartItems.value = currentItems
        updateTotalPrice()
        context?.let { Persistence.saveCart(it, currentItems) }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        _totalPrice.value = 0.0
        context?.let { Persistence.saveCart(it, emptyList()) }
    }

    private fun updateTotalPrice() {
        _totalPrice.value = _cartItems.value.sumOf { it.price * it.quantity.toDouble() }
    }

    private fun loadCart(context: Context) {
        val savedItems = Persistence.loadCart(context)
        _cartItems.value = savedItems
        updateTotalPrice()
    }
}
