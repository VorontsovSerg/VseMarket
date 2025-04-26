package com.example.vsemarket.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.vsemarket.data.CartItem
import com.example.vsemarket.data.Persistence
import com.example.vsemarket.data.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartViewModel(private val context: Context? = null) : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice

    init {
        context?.let { loadCart(it) }
    }

    fun addToCart(product: Product) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.product.id == product.id }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            currentItems.add(CartItem(product, 1))
        }
        _cartItems.value = currentItems
        updateTotalPrice()
        context?.let { Persistence.saveCart(it, currentItems) }
    }

    fun updateQuantity(productId: Int, delta: Int) {
        val currentItems = _cartItems.value.toMutableList()
        val item = currentItems.find { it.product.id == productId }
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

    fun removeFromCart(productId: Int) {
        val currentItems = _cartItems.value.toMutableList()
        currentItems.removeAll { it.product.id == productId }
        _cartItems.value = currentItems
        updateTotalPrice()
        context?.let { Persistence.saveCart(it, currentItems) }
    }

    private fun updateTotalPrice() {
        _totalPrice.value = _cartItems.value.sumOf { it.product.price * it.quantity }
    }

    private fun loadCart(context: Context) {
        val savedItems = Persistence.loadCart(context)
        _cartItems.value = savedItems
        updateTotalPrice()
    }
}